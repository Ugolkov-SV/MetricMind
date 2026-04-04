package validation

import io.ugolkov.metric_mind.biz.validation.validateDateNotFuture
import io.ugolkov.metric_mind.common.TrackContext
import io.ugolkov.metric_mind.common.model.MmState
import io.ugolkov.metric_mind.common.model.MmTrack
import io.ugolkov.metric_mind.cor.rootChain
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidateDateNotFutureTest {
    @Test
    fun correct() =
        runTest {
            val ctx = TrackContext(state = MmState.RUNNING)
                .apply { validating = MmTrack(createDate = 2026) }

            rootChain<TrackContext> {
                validateDateNotFuture(
                    title = "Title",
                    field = "createDate",
                    selector = { validating.createDate }
                )
            }.exec(ctx)

            assertEquals(MmState.RUNNING, ctx.state)
            assertEquals(0, ctx.errors.size)
        }

    @Test
    fun future() =
        runTest {
            val ctx = TrackContext(state = MmState.RUNNING)
                .apply { validating = MmTrack(createDate = Long.MAX_VALUE) }

            rootChain<TrackContext> {
                validateDateNotFuture(
                    title = "Title",
                    field = "createDate",
                    selector = { validating.createDate }
                )
            }.exec(ctx)

            assertEquals(MmState.FAILING, ctx.state)
            assertEquals(1, ctx.errors.size)
        }
}