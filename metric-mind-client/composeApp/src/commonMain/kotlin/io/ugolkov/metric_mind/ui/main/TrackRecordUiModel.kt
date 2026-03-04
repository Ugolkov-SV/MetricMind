package io.ugolkov.metric_mind.ui.main

import androidx.compose.runtime.Immutable

@Immutable
data class TrackRecordUiModel(
    val id: Long,
    val value: String,
    val date: String,
    val time: String,
)
