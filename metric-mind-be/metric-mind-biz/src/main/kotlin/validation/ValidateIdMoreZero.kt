package io.ugolkov.metric_mind.biz.validation

import io.ugolkov.metric_mind.biz.helper.errorValidation
import io.ugolkov.metric_mind.biz.helper.fail
import io.ugolkov.metric_mind.common.BaseContext
import io.ugolkov.metric_mind.common.model.MmTrackId
import io.ugolkov.metric_mind.cor.IChainDsl
import io.ugolkov.metric_mind.cor.worker

internal fun <T : BaseContext> IChainDsl<T>.validateIdMoreZero(
    title: String,
    field: String,
    selector: T.() -> MmTrackId,
) =
    worker {
        this.title = title
        on { selector().asLong() <= 0L }
        handle {
            fail(
                errorValidation(
                    field = field,
                    violationCode = "empty",
                    description = "field must be more than zero"
                )
            )
        }
    }