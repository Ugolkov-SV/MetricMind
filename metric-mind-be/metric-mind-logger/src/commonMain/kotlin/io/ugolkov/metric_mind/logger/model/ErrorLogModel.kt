package io.ugolkov.metric_mind.logger.model

import kotlinx.serialization.Serializable

@Serializable
data class ErrorLogModel(
    val code: String? = null,
    val message: String? = null,
    val field: String? = null,
)
