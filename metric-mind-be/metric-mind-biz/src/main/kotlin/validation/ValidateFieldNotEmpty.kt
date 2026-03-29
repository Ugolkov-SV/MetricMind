package io.ugolkov.metric_mind.biz.validation

import io.ugolkov.metric_mind.biz.helper.errorValidation
import io.ugolkov.metric_mind.biz.helper.fail
import io.ugolkov.metric_mind.common.MmContext
import io.ugolkov.metric_mind.cor.IChainDsl
import io.ugolkov.metric_mind.cor.worker

internal fun IChainDsl<MmContext>.validateFieldNotEmpty(
    title: String,
    field: String,
    selector: MmContext.() -> String,
) =
    worker {
        this.title = title
        on { selector().isBlank() }
        handle {
            fail(
                errorValidation(
                    field = field,
                    violationCode = "empty",
                    description = "field must not be empty"
                )
            )
        }
    }