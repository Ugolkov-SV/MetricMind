package io.ugolkov.metric_mind.spring.helper

import io.ugolkov.metric_mind.common.model.MmError

fun Throwable.asMmError(
    code: String = "unknown",
    message: String = this.message ?: "",
) = MmError(
    code = code,
    field = "",
    message = message,
    exception = this,
)