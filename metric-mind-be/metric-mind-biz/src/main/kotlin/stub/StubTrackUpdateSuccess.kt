package io.ugolkov.metric_mind.biz.stub

import io.ugolkov.metric_mind.common.MmCorSettings
import io.ugolkov.metric_mind.common.TrackContext
import io.ugolkov.metric_mind.common.model.*
import io.ugolkov.metric_mind.cor.IChainDsl
import io.ugolkov.metric_mind.cor.worker
import io.ugolkov.metric_mind.logger.base.LogLevel
import io.ugolkov.metric_mind.stubs.MmTrackStub

internal fun IChainDsl<TrackContext>.stubTrackUpdateSuccess(title: String, corSettings: MmCorSettings) =
    worker {
        this.title = title
        on { stubCase == MmStubs.SUCCESS && state == MmState.RUNNING }
        val logger = corSettings.loggerProvider.logger("stubTrackUpdateSuccess")
        handle {
            logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
                state = MmState.FINISHING
                MmTrackStub.prepareResult {
                    request.id.takeIf { it != MmTrackId.NONE }?.also { this.id = it }
                    request.title.takeIf { it.isNotBlank() }?.also { this.title = it }
                    request.type.takeIf { it != MmTrackType.NONE }?.also { this.type = it }
                    request.createDate.takeIf { it != 0L }?.also { this.createDate = it }
                    request.unit.takeIf { it.isNotBlank() }?.also { this.unit = it }
                    request.visibility.takeIf { it != MmVisibility.NONE }?.also { this.visibility = it }
                    request.owner.takeIf { it != MmUserId.NONE }?.also { this.owner = it }
                }
                    .let(response::add)
            }
        }
    }
