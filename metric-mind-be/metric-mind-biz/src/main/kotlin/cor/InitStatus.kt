package io.ugolkov.metric_mind.biz.cor

import io.ugolkov.metric_mind.common.BaseContext
import io.ugolkov.metric_mind.common.model.MmState
import io.ugolkov.metric_mind.cor.IChainDsl
import io.ugolkov.metric_mind.cor.worker

internal fun <T : BaseContext> IChainDsl<T>.initStatus(title: String) =
    worker {
        this.title = title
        this.description = "Стартовый обработчик"

        on { state == MmState.NONE }
        handle { state = MmState.RUNNING }
    }