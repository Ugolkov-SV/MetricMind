package io.ugolkov.metric_mind.common.model

data class MmError(
    val code: String = "",
    val field: String = "",
    val message: String = "",
    val exception: Throwable? = null,
)
