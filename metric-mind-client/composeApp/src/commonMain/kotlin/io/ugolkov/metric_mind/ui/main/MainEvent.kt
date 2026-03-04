package io.ugolkov.metric_mind.ui.main

import io.ugolkov.metric_mind.data.model.TrackType
import io.ugolkov.metric_mind.data.model.Visibility
import io.ugolkov.metric_mind.ui.common.Event
import kotlinx.datetime.LocalDateTime
import kotlin.jvm.JvmInline

sealed interface MainEvent : Event {
    @JvmInline
    value class TrackSelect(val id: Long) : MainEvent

    data class TrackCreate(
        val title: String,
        val type: TrackType,
        val unit: String,
        val visibility: Visibility,
    ) : MainEvent

    @JvmInline
    value class TrackDelete(val id: Long) : MainEvent

    data class TrackRecordCreate(
        val value: String,
        val dateTime: LocalDateTime,
    ) : MainEvent

    @JvmInline
    value class TrackRecordDelete(val id: Long) : MainEvent
}