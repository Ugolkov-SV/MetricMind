package io.ugolkov.metric_mind.biz.validation

import io.ugolkov.metric_mind.biz.helper.errorValidation
import io.ugolkov.metric_mind.biz.helper.fail
import io.ugolkov.metric_mind.common.MmContext
import io.ugolkov.metric_mind.cor.IChainDsl
import io.ugolkov.metric_mind.cor.worker

internal fun IChainDsl<MmContext>.validateDateNotFuture(
    title: String,
    field: String,
    selector: MmContext.() -> Long,
) =
    worker {
        this.title = title
        on { selector() > System.currentTimeMillis() }
        handle {
            fail(
                errorValidation(
                    field = field,
                    violationCode = "empty",
                    description = "date must not be in future"
                )
            )
        }
    }