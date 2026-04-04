import io.ugolkov.api.v1.models.*
import io.ugolkov.metric_mind.api.v1.mappers.fromTransport
import io.ugolkov.metric_mind.api.v1.mappers.toTransport
import io.ugolkov.metric_mind.common.TrackRecordContext
import io.ugolkov.metric_mind.common.model.*
import io.ugolkov.metric_mind.stubs.MmTrackRecordStub
import kotlin.test.Test
import kotlin.test.assertEquals

class MapperUpdateTrackRecordTest {
    @Test
    fun fromTransport() {
        val request = TrackRecordUpdateRq(
            debug = Debug(
                mode = Mode.STUB,
                stub = Stubs.SUCCESS,
            ),
            trackRecord = MmTrackRecordStub.get().toTransport(),
        )
        val context = TrackRecordContext()
        context.fromTransport(request)

        val expected = MmTrackRecordStub.prepareResult {
            trackRecordId = MmTrackId.NONE
        }
        assertEquals(MmStubs.SUCCESS, context.stubCase)
        assertEquals(MmWorkMode.STUB, context.workMode)
        assertEquals(expected, context.request)
    }

    @Test
    fun toTransport() {
        val context = TrackRecordContext().apply {
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

            request = MmTrackRecordStub.get()
        }

        val response = context.toTransport() as TrackRecordUpdateRs

        assertEquals(1, response.errors?.size)
        assertEquals("err", response.errors?.firstOrNull()?.code)
        assertEquals("title", response.errors?.firstOrNull()?.field)
        assertEquals("wrong title", response.errors?.firstOrNull()?.message)
    }
}
