package io.ugolkov.metric_mind.biz

import io.ugolkov.metric_mind.common.IProcessor
import io.ugolkov.metric_mind.common.MmContext
import io.ugolkov.metric_mind.common.MmCorSettings
import io.ugolkov.metric_mind.common.model.MmState
import io.ugolkov.metric_mind.stubs.MmTrackRecordStub
import io.ugolkov.metric_mind.stubs.MmTrackStub

@Suppress("unused")
class MmProcessor(val corSettings: MmCorSettings) : IProcessor {
    override suspend fun exec(ctx: MmContext) {
        when {
            !ctx.trackRequest.isEmpty() ||
                    !ctx.trackFilterRequest.isEmpty() -> handleTrack(ctx)

            !ctx.trackRecordRequest.isEmpty() -> handleTrackRecord(ctx)
            else -> {
                ctx.state = MmState.FAILING
                return
            }
        }
        ctx.state = MmState.RUNNING
    }

    private fun handleTrack(ctx: MmContext) {
        ctx.trackResponse.add(MmTrackStub.get())
    }

    private fun handleTrackRecord(ctx: MmContext) {
        ctx.trackRecordResponse.add(MmTrackRecordStub.get())
    }
}