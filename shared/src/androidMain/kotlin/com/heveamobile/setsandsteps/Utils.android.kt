package com.heveamobile.setsandsteps

import android.text.format.DateFormat.getBestDateTimePattern
import kotlinx.datetime.LocalTime
import kotlinx.datetime.toJavaLocalTime
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.sql.Date
import java.text.DateFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale
import kotlin.time.Instant
import kotlin.time.toJavaInstant


actual fun formatAmount(
    amount: Long,
    formatMode: FormatMode,
): String {
    val numberFormatter = NumberFormat.getInstance(Locale.getDefault())
    when (formatMode) {
        FormatMode.Short -> {
            val shortNumber = amount.floorDiv(1_000)
            if (shortNumber < 1_000L) {
                return "${numberFormatter.format(shortNumber)}K"
            }
            return "${numberFormatter.format(shortNumber.floorDiv(1_000))}M"
        }

        FormatMode.Medium -> {
            if (amount < 1_000L) {
                return "${numberFormatter.format(amount)}"
            }
            val shortNumber = amount.floorDiv(1_000)
            if (shortNumber < 10_000L) {
                return "${numberFormatter.format(shortNumber)}K"
            }
            return "${numberFormatter.format(shortNumber.floorDiv(1_000))}M"
        }

        FormatMode.Long -> {
            return numberFormatter.format(amount)
        }
    }
}

actual fun formatDate(
    instant: Instant,
    formatMode: FormatMode,
): String {
    val date = java.util.Date.from(instant.toJavaInstant())

    return if (formatMode == FormatMode.Short) {
        // "MMd" provides a localized pattern for Month and Day without the Year
        val pattern = getBestDateTimePattern(
            Locale.getDefault(),
            "MMd",
        )
        val simpleDateFormat = SimpleDateFormat(
            pattern,
            Locale.getDefault(),
        )
        simpleDateFormat.format(date)
    } else {
        val format = when (formatMode) {
            FormatMode.Medium -> DateFormat.MEDIUM
            FormatMode.Long -> DateFormat.LONG
        }
        DateFormat
            .getDateInstance(
                format,
                Locale.getDefault(),
            )
            .format(date)
    }
}

actual fun formatTime(
    instant: Instant,
    formatMode: FormatMode,
): String {
    val format = when (formatMode) {
        FormatMode.Short -> DateFormat.SHORT
        FormatMode.Medium -> DateFormat.MEDIUM
        FormatMode.Long -> DateFormat.LONG
    }

    val date = Date.from(instant.toJavaInstant())
    return DateFormat
        .getTimeInstance(
            format,
            Locale.getDefault(),
        )
        .format(date)

}

actual fun formatTime(
    localTime: LocalTime,
    formatMode: FormatMode,
): String {
    val format = when (formatMode) {
        FormatMode.Short -> FormatStyle.SHORT
        FormatMode.Medium -> FormatStyle.MEDIUM
        FormatMode.Long -> FormatStyle.MEDIUM // LONG requires TimeZone which LocalTime doesn't have
    }

    val formatter = DateTimeFormatter
        .ofLocalizedTime(format)
        .withLocale(Locale.getDefault())

    return localTime
        .toJavaLocalTime()
        .format(formatter)
}

actual fun encodeUrl(url: String): String {
    return URLEncoder
        .encode(
            url,
            StandardCharsets.UTF_8.toString(),
        )
        .replace(
            "+",
            "",
        )
}