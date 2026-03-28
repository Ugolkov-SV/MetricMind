package io.ugolkov.metric_mind.biz.cor

import io.ugolkov.metric_mind.common.MmContext
import io.ugolkov.metric_mind.common.model.MmCommand
import io.ugolkov.metric_mind.common.model.MmState
import io.ugolkov.metric_mind.cor.IChainDsl
import io.ugolkov.metric_mind.cor.chain

internal fun IChainDsl<MmContext>.operation(
    title: String,
    command: MmCommand,
    block: IChainDsl<MmContext>.() -> Unit,
) =
    chain {
        this.title = title
        on { this.command == command && state == MmState.RUNNING }
        block()
    }