import io.ugolkov.api.v1.models.*
import io.ugolkov.metric_mind.api.v1.mappers.fromTransport
import io.ugolkov.metric_mind.api.v1.mappers.toTransport
import io.ugolkov.metric_mind.common.TrackContext
import io.ugolkov.metric_mind.common.model.*
import io.ugolkov.metric_mind.stubs.MmTrackStub
import kotlin.test.Test
import kotlin.test.assertEquals

class MapperCreateTrackTest {
    @Test
    fun fromTransport() {
        val request = TrackCreateRq(
            debug = Debug(
                mode = Mode.STUB,
                stub = Stubs.SUCCESS,
            ),
            track = MmTrackStub.get().toTransport(),
        )

        val context = TrackContext()
        context.fromTransport(request)

        val expected = MmTrackStub.prepareResult {
            id = MmTrackId.NONE
            owner = MmUserId.NONE
        }
        assertEquals(MmStubs.SUCCESS, context.stubCase)
        assertEquals(MmWorkMode.STUB, context.workMode)
        assertEquals(expected, context.request)
    }

    @Test
    fun toTransport() {
        val context = TrackContext().apply {
            command = MmCommand.CREATE
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

        val response = context.toTransport() as TrackCreateRs

        assertEquals(MmTrackStub.get().id.toTransport(), response.track)
        assertEquals(1, response.errors?.size)
        assertEquals("err", response.errors?.firstOrNull()?.code)
        assertEquals("title", response.errors?.firstOrNull()?.field)
        assertEquals("wrong title", response.errors?.firstOrNull()?.message)
    }

    private fun MmTrack.toTransport(): TrackWrite =
        TrackWrite(
            title = this.title,
            type = this.type.toTransport(),
            createDate = this.createDate,
            visibility = this.visibility.toTransport(),
            unit = this.unit,
        )
}
