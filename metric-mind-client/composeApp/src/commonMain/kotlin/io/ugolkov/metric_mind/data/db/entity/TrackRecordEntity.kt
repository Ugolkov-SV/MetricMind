package io.ugolkov.metric_mind.data.db.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

@Entity(tableName = "track_record")
data class TrackRecordEntity(
    val trackId: Long,
    val value: String,
    val dateTime: LocalDateTime,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
) {
    @Ignore
    val date: LocalDate = dateTime.date

    @Ignore
    val time: LocalTime = dateTime.time
}
