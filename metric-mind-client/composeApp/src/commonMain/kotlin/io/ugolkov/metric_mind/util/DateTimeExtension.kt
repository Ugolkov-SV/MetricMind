package io.ugolkov.metric_mind.util

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant

fun LocalDateTime.Companion.now(): LocalDateTime =
    Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())

@OptIn(FormatStringsInDatetimeFormats::class)
private val timeFormat: DateTimeFormat<LocalTime> =
    LocalTime.Format {
        byUnicodePattern("HH:mm")
    }

fun LocalTime.format(): String =
    this.format(timeFormat)

@OptIn(FormatStringsInDatetimeFormats::class)
private val dateFormat: DateTimeFormat<LocalDate> =
    LocalDate.Format {
        byUnicodePattern("yyyy-MM-dd")
    }

fun LocalDate.format(): String =
    this.format(dateFormat)

fun LocalDate.toTimestampMillis(): Long =
    this.atStartOfDayIn(TimeZone.UTC)
        .toEpochMilliseconds()

fun Long.toLocalDate(): LocalDate =
    Instant.fromEpochMilliseconds(this)
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .date