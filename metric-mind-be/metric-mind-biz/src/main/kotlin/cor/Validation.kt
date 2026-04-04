package io.ugolkov.metric_mind.biz.cor

import io.ugolkov.metric_mind.common.BaseContext
import io.ugolkov.metric_mind.common.model.MmState
import io.ugolkov.metric_mind.cor.IChainDsl
import io.ugolkov.metric_mind.cor.chain

internal fun <T : BaseContext> IChainDsl<T>.validation(
    block: IChainDsl<T>.() -> Unit,
) =
    chain {
        this.title = "Валидация"
        on { state == MmState.RUNNING }
        block()
    }