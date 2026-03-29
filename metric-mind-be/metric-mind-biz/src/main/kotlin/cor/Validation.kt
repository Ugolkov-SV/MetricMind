package io.ugolkov.metric_mind.biz.cor

import io.ugolkov.metric_mind.common.MmContext
import io.ugolkov.metric_mind.common.model.MmState
import io.ugolkov.metric_mind.cor.IChainDsl
import io.ugolkov.metric_mind.cor.chain

internal fun IChainDsl<MmContext>.validation(
    block: IChainDsl<MmContext>.() -> Unit,
) =
    chain {
        this.title = "Валидация"
        on { state == MmState.RUNNING }
        block()
    }