package io.ugolkov.metric_mind.biz.stub

import io.ugolkov.metric_mind.common.MmCorSettings
import io.ugolkov.metric_mind.common.TrackContext
import io.ugolkov.metric_mind.common.model.MmState
import io.ugolkov.metric_mind.common.model.MmStubs
import io.ugolkov.metric_mind.common.model.MmTrackId
import io.ugolkov.metric_mind.cor.IChainDsl
import io.ugolkov.metric_mind.cor.worker
import io.ugolkov.metric_mind.logger.base.LogLevel
import io.ugolkov.metric_mind.stubs.MmTrackStub

internal fun IChainDsl<TrackContext>.stubTrackDeleteSuccess(title: String, corSettings: MmCorSettings) =
    worker {
        this.title = title
        on { stubCase == MmStubs.SUCCESS && state == MmState.RUNNING }
        val logger = corSettings.loggerProvider.logger("stubTrackDeleteSuccess")
        handle {
            logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
                state = MmState.FINISHING
                MmTrackStub.prepareResult {
                    request.id.takeIf { it != MmTrackId.NONE }?.also { this.id = it }
                }
                    .let(response::add)
            }
        }
    }
