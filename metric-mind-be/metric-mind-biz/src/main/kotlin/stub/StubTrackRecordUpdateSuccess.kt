package io.ugolkov.metric_mind.biz.stub

import io.ugolkov.metric_mind.common.MmCorSettings
import io.ugolkov.metric_mind.common.TrackRecordContext
import io.ugolkov.metric_mind.common.model.MmState
import io.ugolkov.metric_mind.common.model.MmStubs
import io.ugolkov.metric_mind.common.model.MmTrackId
import io.ugolkov.metric_mind.cor.IChainDsl
import io.ugolkov.metric_mind.cor.worker
import io.ugolkov.metric_mind.logger.base.LogLevel
import io.ugolkov.metric_mind.stubs.MmTrackRecordStub

internal fun IChainDsl<TrackRecordContext>.stubTrackRecordUpdateSuccess(title: String, corSettings: MmCorSettings) =
    worker {
        this.title = title
        on { stubCase == MmStubs.SUCCESS && state == MmState.RUNNING }
        val logger = corSettings.loggerProvider.logger("stubTrackRecordUpdateSuccess")
        handle {
            logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
                state = MmState.FINISHING
                MmTrackRecordStub.prepareResult {
                    request.trackRecordId.takeIf { it != MmTrackId.NONE }?.also { this.trackRecordId = it }
                    request.trackId.takeIf { it != MmTrackId.NONE }?.also { this.trackId = it }
                    request.value.takeIf { it != 0.0 }?.also { this.value = it }
                    request.date.takeIf { it != 0L }?.also { this.date = it }
                }
                    .let(response::add)
            }
        }
    }
