package ru.otus.otuskotlin.marketplace.api.v2.mappers

import io.ugolkov.api.v1.models.*
import io.ugolkov.metric_mind.api.v1.mappers.fromTransport
import io.ugolkov.metric_mind.api.v1.mappers.toTransport
import io.ugolkov.metric_mind.common.MmContext
import io.ugolkov.metric_mind.common.model.*
import ru.otus.otuskotlin.marketplace.stubs.MmTrackStub
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
        val expected = MmTrackStub
            .prepareResult {
                owner = MmUserId.NONE
                type = MmTrackType.NONE
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
            command = MmCommand.UPDATE,
            trackRequest = MmTrackStub.get(),
            errors = mutableListOf(
                MmError(
                    code = "err",
                    field = "title",
                    message = "wrong title",
                )
            ),
            state = MmState.RUNNING,
        )

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
