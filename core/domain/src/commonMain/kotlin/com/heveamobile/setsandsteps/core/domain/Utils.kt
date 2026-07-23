package com.heveamobile.setsandsteps.core.domain

import kotlinx.datetime.LocalTime
import kotlin.time.Instant

enum class FormatMode {
    Short,
    Medium,
    Long
}

expect fun formatAmount(
    amount: Long,
    formatMode: FormatMode = FormatMode.Long,
): String

fun formatAmount(
    amount: Int,
    formatMode: FormatMode = FormatMode.Long,
): String {
    return formatAmount(
        amount.toLong(),
        formatMode,
    )
}

expect fun formatDate(
    instant: Instant,
    formatMode: FormatMode = FormatMode.Long,
): String

expect fun formatTime(
    instant: Instant,
    formatMode: FormatMode = FormatMode.Long,
): String
expect fun formatTime(
    localTime: LocalTime,
    formatMode: FormatMode = FormatMode.Long,
): String

fun formatDateTime(
    instant: Instant,
    formatMode: FormatMode = FormatMode.Long,
): String {
    return formatDate(
        instant,
        formatMode,
    ) + ", " + formatTime(
        instant,
        formatMode,
    )
}

fun String.toTitleCase(): String {
    return this
        .split(" ")
        .joinToString(" ") { word ->
            word
                .lowercase()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        }
}

expect fun encodeUrl(url: String): String