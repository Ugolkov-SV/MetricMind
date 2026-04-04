import io.ugolkov.api.v1.models.*
import io.ugolkov.metric_mind.api.v1.mappers.fromTransport
import io.ugolkov.metric_mind.api.v1.mappers.toTransport
import io.ugolkov.metric_mind.common.TrackContext
import io.ugolkov.metric_mind.common.model.*
import io.ugolkov.metric_mind.stubs.MmTrackStub
import kotlin.test.Test
import kotlin.test.assertEquals

class MapperUpdateTrackTest {
    @Test
    fun fromTransport() {
        val request = TrackUpdateRq(
            debug = Debug(
                mode = Mode.STUB,
                stub = Stubs.SUCCESS,
            ),
            track = MmTrackStub.get().toTransport(),
        )

        val context = TrackContext()
        context.fromTransport(request)

        val expected = MmTrackStub.prepareResult {
            owner = MmUserId.NONE
            type = MmTrackType.NONE
        }
        assertEquals(MmStubs.SUCCESS, context.stubCase)
        assertEquals(MmWorkMode.STUB, context.workMode)
        assertEquals(expected, context.request)
    }

    @Test
    fun toTransport() {
        val context = TrackContext().apply {
            command = MmCommand.UPDATE
            state = MmState.RUNNING
            requestId = MmRequestId("1234")

            errors.add(
                MmError(
                    code = "err",
                    field = "title",
                    message = "wrong title",
                )
            )

            request = MmTrackStub.get()
        }

        val response = context.toTransport() as TrackUpdateRs

        assertEquals(1, response.errors?.size)
        assertEquals("err", response.errors?.firstOrNull()?.code)
        assertEquals("title", response.errors?.firstOrNull()?.field)
        assertEquals("wrong title", response.errors?.firstOrNull()?.message)
    }

    private fun MmTrack.toTransport(): TrackUpdateRqAllOfTrack =
        TrackUpdateRqAllOfTrack(
            id = this.id.asLong(),
            title = this.title,
            unit = this.unit,
            visibility = this.visibility.toTransport(),
        )
}
