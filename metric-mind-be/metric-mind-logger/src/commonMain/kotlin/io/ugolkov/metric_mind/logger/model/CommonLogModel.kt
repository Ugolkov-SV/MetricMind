package io.ugolkov.metric_mind.logger.model

import kotlinx.serialization.Serializable

@Serializable
data class CommonLogModel<T>(
    val messageTime: String? = null,
    val logId: String? = null,
    val source: String? = null,
    val model: T? = null,
    val errors: List<ErrorLogModel>? = null
)