package io.ugolkov.metric_mind.biz.helper

import io.ugolkov.metric_mind.common.MmContext
import io.ugolkov.metric_mind.common.model.MmError
import io.ugolkov.metric_mind.common.model.MmState

fun Throwable.asMmError(
    code: String = "unknown",
    message: String = this.message ?: "",
) = MmError(
    code = code,
    field = "",
    message = message,
    exception = this,
)

inline fun MmContext.fail(error: MmError) {
    errors.add(error)
    state = MmState.FAILING
}