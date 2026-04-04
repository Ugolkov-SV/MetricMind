package io.ugolkov.metric_mind.biz.stub

import io.ugolkov.metric_mind.biz.helper.fail
import io.ugolkov.metric_mind.common.BaseContext
import io.ugolkov.metric_mind.common.model.MmError
import io.ugolkov.metric_mind.common.model.MmState
import io.ugolkov.metric_mind.common.model.MmStubs
import io.ugolkov.metric_mind.cor.IChainDsl
import io.ugolkov.metric_mind.cor.worker

internal fun <T : BaseContext> IChainDsl<T>.stubValidationBadValue(title: String) =
    worker {
        this.title = title
        on { stubCase == MmStubs.BAD_VALUE && state == MmState.RUNNING }
        handle {
            fail(
                MmError(
                    code = "validation-value",
                    field = "value",
                    message = "Wrong value field"
                )
            )
        }
    }
