package io.ugolkov.metric_mind.biz.cor

import io.ugolkov.metric_mind.common.BaseContext
import io.ugolkov.metric_mind.common.model.MmState
import io.ugolkov.metric_mind.common.model.MmWorkMode
import io.ugolkov.metric_mind.cor.IChainDsl
import io.ugolkov.metric_mind.cor.chain

internal fun <T : BaseContext> IChainDsl<T>.stub(
    title: String,
    block: IChainDsl<T>.() -> Unit,
) =
    chain {
        this.title = title
        on { workMode == MmWorkMode.STUB && state == MmState.RUNNING }
        block()
    }