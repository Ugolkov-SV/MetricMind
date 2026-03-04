package io.ugolkov.metric_mind.ui.main

import androidx.compose.runtime.Immutable
import io.ugolkov.metric_mind.data.model.TrackType

@Immutable
data class TrackUiModel(
    val id: Long,
    val title: String,
    val trackType: TrackType,
)
