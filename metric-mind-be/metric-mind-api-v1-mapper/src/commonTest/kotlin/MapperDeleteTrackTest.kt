import io.ugolkov.api.v1.models.*
import io.ugolkov.metric_mind.api.v1.mappers.fromTransport
import io.ugolkov.metric_mind.api.v1.mappers.toTransport
import io.ugolkov.metric_mind.common.TrackContext
import io.ugolkov.metric_mind.common.model.*
import io.ugolkov.metric_mind.stubs.MmTrackStub
import kotlin.test.Test
import kotlin.test.assertEquals

class MapperDeleteTrackTest {
    @Test
    fun fromTransport() {
        val track = MmTrackStub.get()
        val request = TrackDeleteRq(
            debug = Debug(
                mode = Mode.STUB,
                stub = Stubs.SUCCESS,
            ),
            track = track.id.toTransport(),
        )

        val context = TrackContext()
        context.fromTransport(request)

        assertEquals(MmStubs.SUCCESS, context.stubCase)
        assertEquals(MmWorkMode.STUB, context.workMode)
        assertEquals(track.id, context.request.id)
    }

    @Test
    fun toTransport() {
        val context = TrackContext().apply {
            command = MmCommand.DELETE
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
            response.add(MmTrackStub.get())
        }

        val response = context.toTransport() as TrackDeleteRs

        assertEquals(1, response.errors?.size)
        assertEquals("err", response.errors?.firstOrNull()?.code)
        assertEquals("title", response.errors?.firstOrNull()?.field)
        assertEquals("wrong title", response.errors?.firstOrNull()?.message)
    }
}
