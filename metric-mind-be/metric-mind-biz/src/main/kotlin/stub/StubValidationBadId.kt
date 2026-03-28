package io.ugolkov.metric_mind.biz.stub

import io.ugolkov.metric_mind.biz.helper.fail
import io.ugolkov.metric_mind.common.MmContext
import io.ugolkov.metric_mind.common.model.MmError
import io.ugolkov.metric_mind.common.model.MmState
import io.ugolkov.metric_mind.common.model.MmStubs
import io.ugolkov.metric_mind.cor.IChainDsl
import io.ugolkov.metric_mind.cor.worker

internal fun IChainDsl<MmContext>.stubValidationBadId(title: String) =
    worker {
        this.title = title
        on { stubCase == MmStubs.BAD_ID && state == MmState.RUNNING }
        handle {
            fail(
                MmError(
                    code = "validation-id",
                    field = "id",
                    message = "Wrong id field"
                )
            )
        }
    }
