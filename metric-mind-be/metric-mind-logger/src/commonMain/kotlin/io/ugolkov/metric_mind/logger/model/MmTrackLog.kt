package io.ugolkov.metric_mind.logger.model

import kotlinx.serialization.Serializable

@Serializable
data class MmTrackLog(
    val id: String? = null,
    val title: String? = null,
    val type: String? = null,
    val createDate: String? = null,
    val unit: String? = null,
    val visibility: String? = null,
    val owner: String? = null,
)
