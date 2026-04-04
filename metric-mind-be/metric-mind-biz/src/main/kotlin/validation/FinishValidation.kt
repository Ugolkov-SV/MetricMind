package io.ugolkov.metric_mind.biz.validation

import io.ugolkov.metric_mind.common.TrackContext
import io.ugolkov.metric_mind.common.TrackFilterContext
import io.ugolkov.metric_mind.common.TrackRecordContext
import io.ugolkov.metric_mind.common.model.MmState
import io.ugolkov.metric_mind.cor.IChainDsl
import io.ugolkov.metric_mind.cor.worker

internal fun IChainDsl<TrackContext>.finishTrackValidation(title: String) =
    worker {
        this.title = title
        on { state == MmState.RUNNING }
        handle { validated = validating }
    }

internal fun IChainDsl<TrackRecordContext>.finishTrackRecordValidation(title: String) =
    worker {
        this.title = title
        on { state == MmState.RUNNING }
        handle { validated = validating }
    }

internal fun IChainDsl<TrackFilterContext>.finishTrackFilterValidation(title: String) =
    worker {
        this.title = title
        on { state == MmState.RUNNING }
        handle { validated = validating }
    }