package io.ugolkov.metric_mind.spring.common

import io.ugolkov.metric_mind.common.MmContext
import io.ugolkov.metric_mind.common.MmCorSettings
import io.ugolkov.metric_mind.common.model.MmState
import ru.otus.otuskotlin.marketplace.stubs.MmTrackRecordStub

@Suppress("unused")
class MmTrackRecordProcessor(val corSettings: MmCorSettings) : MmProcessor {
    override suspend fun exec(ctx: MmContext) {
        ctx.trackRecordResponse.add(MmTrackRecordStub.get())
        ctx.state = MmState.RUNNING
    }
}