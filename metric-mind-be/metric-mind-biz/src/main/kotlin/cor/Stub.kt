package io.ugolkov.metric_mind.biz.cor

import io.ugolkov.metric_mind.common.MmContext
import io.ugolkov.metric_mind.common.model.MmState
import io.ugolkov.metric_mind.common.model.MmWorkMode
import io.ugolkov.metric_mind.cor.IChainDsl
import io.ugolkov.metric_mind.cor.chain

internal fun IChainDsl<MmContext>.stub(
    title: String,
    block: IChainDsl<MmContext>.() -> Unit,
) =
    chain {
        this.title = title
        on { workMode == MmWorkMode.STUB && state == MmState.RUNNING }
        block()
    }