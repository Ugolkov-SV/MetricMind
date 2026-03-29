package validation

import io.ugolkov.metric_mind.biz.validation.validateDateNotFuture
import io.ugolkov.metric_mind.common.MmContext
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
            val ctx = MmContext(state = MmState.RUNNING, trackValidating = MmTrack(createDate = 2026))
            rootChain {
                validateDateNotFuture(
                    title = "Title",
                    field = "title",
                    selector = { trackValidating.createDate }
                )
            }.exec(ctx)
            assertEquals(MmState.RUNNING, ctx.state)
            assertEquals(0, ctx.errors.size)
        }

    @Test
    fun future() =
        runTest {
            val ctx = MmContext(state = MmState.RUNNING, trackValidating = MmTrack(createDate = Long.MAX_VALUE))
            rootChain {
                validateDateNotFuture(
                    title = "Title",
                    field = "title",
                    selector = { trackValidating.createDate }
                )
            }.exec(ctx)
            assertEquals(MmState.FAILING, ctx.state)
            assertEquals(1, ctx.errors.size)
        }
}