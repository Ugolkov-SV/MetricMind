package io.ugolkov.metric_mind.biz.validation

import io.ugolkov.metric_mind.biz.helper.errorValidation
import io.ugolkov.metric_mind.biz.helper.fail
import io.ugolkov.metric_mind.common.TrackRecordContext
import io.ugolkov.metric_mind.cor.IChainDsl
import io.ugolkov.metric_mind.cor.worker

internal fun IChainDsl<TrackRecordContext>.validateValueNotEmpty(
    title: String,
) =
    worker {
        this.title = title
        on { validating.value.isNaN() }
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