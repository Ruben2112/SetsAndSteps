package com.heveamobile.setsandsteps.core.database

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import androidx.sqlite.execSQL
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals

private const val TEST_DB_NAME = "migration-test"

@RunWith(AndroidJUnit4::class)
class MigrationTest {

    @get:Rule
    val helper = MigrationTestHelper(
        instrumentation = InstrumentationRegistry.getInstrumentation(),
        file = InstrumentationRegistry.getInstrumentation().targetContext.getDatabasePath(TEST_DB_NAME),
        driver = BundledSQLiteDriver(),
        databaseClass = AppDatabase::class,
    )

    @Test
    fun migrate1To2_movesAvailableStepsIntoUnitedNationsCardSet() {
        helper
            .createDatabase(1)
            .apply {
                execSQL(
                    """
                INSERT INTO UserEntity (
                    id, startTime, lastSyncTime, availableSteps, totalSteps,
                    previousTwentyFourHours, twentyFourHourRecord,
                    previousSevenDays, sevenDayRecord,
                    previousThirtyDays, thirtyDayRecord
                ) VALUES (1, 0, NULL, 500, 500, 0, 0, 0, 0, 0, 0)
                """.trimIndent(),
                )
                close()
            }

        helper
            .runMigrationsAndValidate(
                2,
                listOf(MIGRATION_1_2),
            )
            .apply {
                val statement = prepare(
                    "SELECT currentSteps FROM CardSetUserDataEntity WHERE id = '$UNITED_NATIONS_CARD_SET_ID'",
                )
                statement.use { statement ->
                    assertEquals(
                        true,
                        statement.step(),
                    )
                    assertEquals(
                        500L,
                        statement.getLong(0),
                    )
                }
                close()
            }
    }
}
