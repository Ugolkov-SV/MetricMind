package io.ugolkov.metric_mind.biz.validation

import io.ugolkov.metric_mind.biz.helper.errorValidation
import io.ugolkov.metric_mind.biz.helper.fail
import io.ugolkov.metric_mind.common.MmContext
import io.ugolkov.metric_mind.cor.IChainDsl
import io.ugolkov.metric_mind.cor.worker

internal fun IChainDsl<MmContext>.validateValueNotEmpty(
    title: String,
) =
    worker {
        this.title = title
        on { trackRecordValidating.value.isNaN() }
        handle {
            fail(
                errorValidation(
                    field = "value",
                    violationCode = "empty",
                    description = "field must not be empty"
                )
            )
        }
    }