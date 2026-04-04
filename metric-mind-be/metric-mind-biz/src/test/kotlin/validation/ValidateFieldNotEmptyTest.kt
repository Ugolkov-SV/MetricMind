package validation

import io.ugolkov.metric_mind.biz.validation.validateFieldNotEmpty
import io.ugolkov.metric_mind.common.TrackContext
import io.ugolkov.metric_mind.common.model.MmState
import io.ugolkov.metric_mind.common.model.MmTrack
import io.ugolkov.metric_mind.cor.rootChain
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidateFieldNotEmptyTest {
    @Test
    fun emptyString() =
        runTest {
            val ctx = TrackContext(state = MmState.RUNNING)
                .apply { validating = MmTrack(title = "") }

            rootChain<TrackContext> {
                validateFieldNotEmpty(
                    title = "Title",
                    field = "title",
                    selector = { validating.title }
                )
            }.exec(ctx)

            assertEquals(MmState.FAILING, ctx.state)
            assertEquals(1, ctx.errors.size)
        }

    @Test
    fun blankString() =
        runTest {
            val ctx = TrackContext(state = MmState.RUNNING)
                .apply { validating = MmTrack(title = "   ") }

            rootChain<TrackContext> {
                validateFieldNotEmpty(
                    title = "Title",
                    field = "title",
                    selector = { validating.title }
                )
            }.exec(ctx)

            assertEquals(MmState.FAILING, ctx.state)
            assertEquals(1, ctx.errors.size)
            assertEquals("validation-title-empty", ctx.errors.first().code)
        }

    @Test
    fun normalString() =
        runTest {
            val ctx = TrackContext(state = MmState.RUNNING)
                .apply { validating = MmTrack(title = "Ж") }

            rootChain<TrackContext> {
                validateFieldNotEmpty(
                    title = "Title",
                    field = "title",
                    selector = { validating.title }
                )
            }.exec(ctx)

            assertEquals(MmState.RUNNING, ctx.state)
            assertEquals(0, ctx.errors.size)
        }
}
