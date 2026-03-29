package io.ugolkov.metric_mind.biz.stub

import io.ugolkov.metric_mind.biz.helper.fail
import io.ugolkov.metric_mind.common.BaseContext
import io.ugolkov.metric_mind.common.model.MmError
import io.ugolkov.metric_mind.common.model.MmState
import io.ugolkov.metric_mind.common.model.MmStubs
import io.ugolkov.metric_mind.cor.IChainDsl
import io.ugolkov.metric_mind.cor.worker

internal fun <T : BaseContext> IChainDsl<T>.stubDbError(title: String) =
    worker {
        this.title = title
        on { stubCase == MmStubs.DB_ERROR && state == MmState.RUNNING }
        handle {
            fail(
                MmError(
                    code = "internal-db",
                    message = "Internal error"
                )
            )
        }
    }
