package io.ugolkov.metric_mind.biz.stub

import io.ugolkov.metric_mind.biz.helper.fail
import io.ugolkov.metric_mind.common.MmContext
import io.ugolkov.metric_mind.common.model.MmError
import io.ugolkov.metric_mind.common.model.MmState
import io.ugolkov.metric_mind.cor.IChainDsl
import io.ugolkov.metric_mind.cor.worker

internal fun IChainDsl<MmContext>.stubNoCase(title: String) =
    worker {
        this.title = title
        on { state == MmState.RUNNING }
        handle {
            fail(
                MmError(
                    code = "validation",
                    field = "stub",
                    message = "Wrong stub case is requested: ${stubCase.name}"
                )
            )
        }
    }
