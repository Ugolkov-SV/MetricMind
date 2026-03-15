import io.ugolkov.api.v1.models.*
import io.ugolkov.metric_mind.api.v1.mappers.fromTransport
import io.ugolkov.metric_mind.api.v1.mappers.toTransport
import io.ugolkov.metric_mind.common.MmContext
import io.ugolkov.metric_mind.common.model.*
import io.ugolkov.metric_mind.ru.stubs.MmTrackStub
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
        val expected = MmTrackStub.prepareResult {
            id = MmTrackId.NONE
            owner = MmUserId.NONE
        }

        val context = MmContext()
        context.fromTransport(request)

        assertEquals(MmStubs.SUCCESS, context.stubCase)
        assertEquals(MmWorkMode.STUB, context.workMode)
        assertEquals(expected, context.trackRequest)
    }

    @Test
    fun toTransport() {
        val context = MmContext(
            requestId = MmRequestId("1234"),
            command = MmCommand.CREATE,
            trackRequest = MmTrackStub.get(),
            trackResponse = mutableListOf(MmTrackStub.get()),
            errors = mutableListOf(
                MmError(
                    code = "err",
                    field = "title",
                    message = "wrong title",
                )
            ),
            state = MmState.RUNNING,
        )

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
