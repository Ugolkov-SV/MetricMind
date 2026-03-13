package io.ugolkov.metric_mind.spring.common

import io.ugolkov.metric_mind.common.MmContext
import io.ugolkov.metric_mind.common.MmCorSettings
import io.ugolkov.metric_mind.common.model.MmState
import ru.otus.otuskotlin.marketplace.stubs.MmTrackStub

@Suppress("unused")
class MmTrackProcessor(val corSettings: MmCorSettings) : MmProcessor {
    override suspend fun exec(ctx: MmContext) {
        ctx.trackResponse.add(MmTrackStub.get())
        ctx.state = MmState.RUNNING
    }
}