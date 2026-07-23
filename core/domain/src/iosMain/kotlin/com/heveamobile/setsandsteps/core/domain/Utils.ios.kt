package com.heveamobile.setsandsteps.core.domain

import kotlinx.datetime.LocalTime
import kotlin.time.Instant

// Thin stub — iOS is not an active target yet, so this favors a simple, dependency-light
// implementation over locale-aware native formatting (see NSDateFormatter/NSNumberFormatter
// if/when iOS work resumes).

actual fun formatAmount(
    amount: Long,
    formatMode: FormatMode,
): String {
    return when (formatMode) {
        FormatMode.Short -> "${amount / 1_000}K"
        FormatMode.Medium -> "${amount / 1_000}K"
        FormatMode.Long -> amount.toString()
    }
}

actual fun formatDate(
    instant: Instant,
    formatMode: FormatMode,
): String {
    return instant.toString()
}

actual fun formatTime(
    instant: Instant,
    formatMode: FormatMode,
): String {
    return instant.toString()
}

actual fun formatTime(
    localTime: LocalTime,
    formatMode: FormatMode,
): String {
    return localTime.toString()
}

actual fun encodeUrl(url: String): String {
    return url
}
