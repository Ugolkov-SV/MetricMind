package io.ugolkov.metric_mind.biz.validation

import io.ugolkov.metric_mind.common.MmContext
import io.ugolkov.metric_mind.common.model.MmState
import io.ugolkov.metric_mind.cor.IChainDsl
import io.ugolkov.metric_mind.cor.worker

internal fun IChainDsl<MmContext>.finishTrackValidation(title: String) =
    worker {
        this.title = title
        on { state == MmState.RUNNING }
        handle {
            trackValidated = trackValidating
        }
    }

internal fun IChainDsl<MmContext>.finishTrackRecordValidation(title: String) =
    worker {
        this.title = title
        on { state == MmState.RUNNING }
        handle {
            trackRecordValidated = trackRecordValidating
        }
    }

internal fun IChainDsl<MmContext>.finishTrackFilterValidation(title: String) =
    worker {
        this.title = title
        on { state == MmState.RUNNING }
        handle {
            trackFilterValidated = trackFilterValidating
        }
    }