import io.ugolkov.api.v1.models.*
import io.ugolkov.metric_mind.api.v1.mappers.fromTransport
import io.ugolkov.metric_mind.api.v1.mappers.toTransport
import io.ugolkov.metric_mind.common.MmContext
import io.ugolkov.metric_mind.common.model.*
import io.ugolkov.metric_mind.stubs.MmTrackRecordStub
import kotlin.test.Test
import kotlin.test.assertEquals

class MapperReadTrackRecordTest {
    @Test
    fun fromTransport() {
        val trackRecord = MmTrackRecordStub.get()
        val request = TrackRecordReadRq(
            debug = Debug(
                mode = Mode.STUB,
                stub = Stubs.SUCCESS,
            ),
            track = trackRecord.trackId.toTransport(),
        )

        val context = MmContext()
        context.fromTransport(request)

        assertEquals(MmStubs.SUCCESS, context.stubCase)
        assertEquals(MmWorkMode.STUB, context.workMode)
        assertEquals(trackRecord.trackId, context.trackRecordRequest.trackId)
    }

    @Test
    fun toTransport() {
        val context = MmContext(
            requestId = MmRequestId("1234"),
            command = MmCommand.READ,
            trackRecordRequest = MmTrackRecordStub.get(),
            trackRecordResponse = mutableListOf(MmTrackRecordStub.get()),
            errors = mutableListOf(
                MmError(
                    code = "err",
                    field = "title",
                    message = "wrong title",
                )
            ),
            state = MmState.RUNNING,
        )

        val response = context.toTransport() as TrackRecordReadRs

        assertEquals(MmTrackRecordStub.get().toTransport(), response.trackRecords?.first())
        assertEquals(1, response.errors?.size)
        assertEquals("err", response.errors?.firstOrNull()?.code)
        assertEquals("title", response.errors?.firstOrNull()?.field)
        assertEquals("wrong title", response.errors?.firstOrNull()?.message)
    }
}
