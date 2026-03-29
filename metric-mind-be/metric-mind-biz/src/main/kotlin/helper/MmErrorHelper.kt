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

inline fun errorValidation(
    field: String,
    /**
     * Код, характеризующий ошибку. Не должен включать имя поля или указание на валидацию.
     * Например: empty, badSymbols, tooLong, etc
     */
    violationCode: String,
    description: String,
) = MmError(
    code = "validation-$field-$violationCode",
    field = field,
    message = "Validation error for field $field: $description",
)