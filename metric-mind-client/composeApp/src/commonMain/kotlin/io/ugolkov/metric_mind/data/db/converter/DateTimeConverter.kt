package io.ugolkov.metric_mind.data.db.converter

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.UtcOffset
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

object DateTimeConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDateTime? {
        return value?.let { Instant.fromEpochSeconds(it).toLocalDateTime(TimeZone.UTC) }
    }

    @TypeConverter
    fun dateTimeToTimestamp(date: LocalDateTime?): Long? {
        return date?.toInstant(UtcOffset.ZERO)?.epochSeconds
    }
}