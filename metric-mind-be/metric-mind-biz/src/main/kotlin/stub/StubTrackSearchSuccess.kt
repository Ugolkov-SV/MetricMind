package io.ugolkov.metric_mind.biz.stub

import io.ugolkov.metric_mind.common.MmCorSettings
import io.ugolkov.metric_mind.common.TrackFilterContext
import io.ugolkov.metric_mind.common.model.MmState
import io.ugolkov.metric_mind.common.model.MmStubs
import io.ugolkov.metric_mind.cor.IChainDsl
import io.ugolkov.metric_mind.cor.worker
import io.ugolkov.metric_mind.logger.base.LogLevel
import io.ugolkov.metric_mind.stubs.MmTrackStub

internal fun IChainDsl<TrackFilterContext>.stubTrackSearchSuccess(title: String, corSettings: MmCorSettings) =
    worker {
        this.title = title
        on { stubCase == MmStubs.SUCCESS && state == MmState.RUNNING }
        val logger = corSettings.loggerProvider.logger("stubTrackSearchSuccess")
        handle {
            logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
                state = MmState.FINISHING
                MmTrackStub.get()
                    .let(response::add)
            }
        }
    }
