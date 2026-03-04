package io.ugolkov.metric_mind.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.ugolkov.metric_mind.data.model.TrackType
import io.ugolkov.metric_mind.data.model.Visibility
import kotlinx.datetime.LocalDateTime

@Entity(tableName = "track")
data class TrackEntity(
    val title: String,
    val type: TrackType,
    val createDate: LocalDateTime,
    val unit: String,
    val visibility: Visibility,
    val order: Int = 0,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
)
