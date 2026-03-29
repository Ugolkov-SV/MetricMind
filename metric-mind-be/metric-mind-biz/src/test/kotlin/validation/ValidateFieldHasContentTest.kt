package validation

import io.ugolkov.metric_mind.biz.validation.validateFieldHasContent
import io.ugolkov.metric_mind.common.MmContext
import io.ugolkov.metric_mind.common.model.MmState
import io.ugolkov.metric_mind.common.model.MmTrack
import io.ugolkov.metric_mind.common.model.MmTrackFilter
import io.ugolkov.metric_mind.cor.rootChain
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidateFieldHasContentTest {
    @Test
    fun emptyString() =
        runTest {
            val ctx = MmContext(state = MmState.RUNNING, trackValidating = MmTrack(title = ""))
            rootChain {
                validateFieldHasContent(
                    title = "Title",
                    field = "title",
                    selector = { trackValidating.title }
                )
            }.exec(ctx)
            assertEquals(MmState.RUNNING, ctx.state)
            assertEquals(0, ctx.errors.size)
        }

    @Test
    fun noContent() =
        runTest {
            val ctx = MmContext(state = MmState.RUNNING, trackValidating = MmTrack(title = "12!@#$%^&*()_+-="))
            rootChain {
                validateFieldHasContent(
                    title = "Title",
                    field = "title",
                    selector = { trackValidating.title }
                )
            }.exec(ctx)
            assertEquals(MmState.FAILING, ctx.state)
            assertEquals(1, ctx.errors.size)
            assertEquals("validation-title-noContent", ctx.errors.first().code)
        }

    @Test
    fun normalString() =
        runTest {
            val ctx = MmContext(state = MmState.RUNNING, trackFilterValidating = MmTrackFilter(searchString = "Ж"))
            rootChain {
                validateFieldHasContent(
                    title = "Title",
                    field = "searchString",
                    selector = { trackFilterValidating.searchString }
                )
            }.exec(ctx)
            assertEquals(MmState.RUNNING, ctx.state)
            assertEquals(0, ctx.errors.size)
        }
}
