import io.ugolkov.api.v1.models.*
import io.ugolkov.metric_mind.api.v1.mappers.fromTransport
import io.ugolkov.metric_mind.api.v1.mappers.toTransport
import io.ugolkov.metric_mind.common.TrackRecordContext
import io.ugolkov.metric_mind.common.model.*
import io.ugolkov.metric_mind.stubs.MmTrackRecordStub
import kotlin.test.Test
import kotlin.test.assertEquals

class MapperDeleteTrackRecordTest {
    @Test
    fun fromTransport() {
        val trackRecord = MmTrackRecordStub.get()
        val request = TrackRecordDeleteRq(
            debug = Debug(
                mode = Mode.STUB,
                stub = Stubs.SUCCESS,
            ),
            trackRecord = trackRecord.toTransport(),
        )

        val context = TrackRecordContext()
        context.fromTransport(request)

        assertEquals(MmStubs.SUCCESS, context.stubCase)
        assertEquals(MmWorkMode.STUB, context.workMode)
        assertEquals(trackRecord.trackRecordId, context.request.trackRecordId)
    }

    @Test
    fun toTransport() {
        val context = TrackRecordContext()
            .apply {
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

                request = MmTrackRecordStub.get()
                response.add(MmTrackRecordStub.get())
            }

        val response = context.toTransport() as TrackRecordDeleteRs

        assertEquals(1, response.errors?.size)
        assertEquals("err", response.errors?.firstOrNull()?.code)
        assertEquals("title", response.errors?.firstOrNull()?.field)
        assertEquals("wrong title", response.errors?.firstOrNull()?.message)
    }

    private fun MmTrackRecord.toTransport(): TrackRecordDeleteRqAllOfTrackRecord =
        TrackRecordDeleteRqAllOfTrackRecord(
            trackRecordId = this.trackRecordId.asLong(),
            date = this.date,
        )
}
