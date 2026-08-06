package com.heveamobile.setsandsteps.core.database

import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL

internal const val UNITED_NATIONS_CARD_SET_ID = "4d08314f-2224-4eff-a3bd-8d141d981fad"

private fun SQLiteConnection.columnExists(
    table: String,
    column: String,
): Boolean {
    val statement = prepare("PRAGMA table_info($table)")
    try {
        while (statement.step()) {
            // PRAGMA table_info columns: cid, name, type, notnull, dflt_value, pk
            if (statement.getText(1) == column) return true
        }
    } finally {
        statement.close()
    }
    return false
}

/**
 * Defensive against builds that ran an earlier draft of this migration during development
 * (before version 2 was rolled out) and already ended up with `currentSteps` added but
 * `availableSteps` not yet dropped. Only performs the steps that are still needed.
 */
val MIGRATION_1_2 = object : Migration(
    1,
    2,
) {
    override fun migrate(connection: SQLiteConnection) {
        if (!connection.columnExists(
                "CardSetUserDataEntity",
                "currentSteps",
            )
        ) {
            connection.execSQL(
                "ALTER TABLE CardSetUserDataEntity ADD COLUMN currentSteps INTEGER NOT NULL DEFAULT 0",
            )
        }

        if (connection.columnExists(
                "UserEntity",
                "availableSteps",
            )
        ) {
            connection.execSQL(
                """
                UPDATE CardSetUserDataEntity
                SET currentSteps = currentSteps + COALESCE((SELECT availableSteps FROM UserEntity LIMIT 1), 0)
                WHERE id = '$UNITED_NATIONS_CARD_SET_ID'
                """.trimIndent(),
            )
            connection.execSQL(
                """
                INSERT INTO CardSetUserDataEntity (id, isActive, isOwned, currentSteps, currentLevel, currentSetPoints)
                SELECT '$UNITED_NATIONS_CARD_SET_ID', 1, 1, availableSteps, 1, 0
                FROM UserEntity
                WHERE NOT EXISTS (SELECT 1 FROM CardSetUserDataEntity WHERE id = '$UNITED_NATIONS_CARD_SET_ID')
                """.trimIndent(),
            )
            connection.execSQL(
                "ALTER TABLE UserEntity DROP COLUMN availableSteps",
            )
        }
    }
}

val ALL_MIGRATIONS = arrayOf(MIGRATION_1_2)
