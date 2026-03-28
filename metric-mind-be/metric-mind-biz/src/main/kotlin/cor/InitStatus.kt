package io.ugolkov.metric_mind.biz.cor

import io.ugolkov.metric_mind.common.MmContext
import io.ugolkov.metric_mind.common.model.MmState
import io.ugolkov.metric_mind.cor.IChainDsl
import io.ugolkov.metric_mind.cor.worker

internal fun IChainDsl<MmContext>.initStatus(title: String) =
    worker {
        this.title = title
        this.description = "Стартовый обработчик"

        on { state == MmState.NONE }
        handle { state = MmState.RUNNING }
    }