package ru.otus.otuskotlin.marketplace.api.v2.mappers

import io.ugolkov.api.v1.models.*
import io.ugolkov.metric_mind.api.v1.mappers.fromTransport
import io.ugolkov.metric_mind.api.v1.mappers.toTransport
import io.ugolkov.metric_mind.common.MmContext
import io.ugolkov.metric_mind.common.model.*
import ru.otus.otuskotlin.marketplace.stubs.MmTrackStub
import kotlin.test.Test
import kotlin.test.assertEquals

class MapperReadTrackTest {
    @Test
    fun fromTransport() {
        val track = MmTrackStub.get()
        val request = TrackReadRq(
            debug = Debug(
                mode = Mode.STUB,
                stub = Stubs.SUCCESS,
            ),
            track = track.id.toTransport(),
        )

        val context = MmContext()
        context.fromTransport(request)

        assertEquals(MmStubs.SUCCESS, context.stubCase)
        assertEquals(MmWorkMode.STUB, context.workMode)
        assertEquals(track.id, context.trackRequest.id)
    }

    @Test
    fun toTransport() {
        val context = MmContext(
            requestId = MmRequestId("1234"),
            command = MmCommand.READ,
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

        val response = context.toTransport() as TrackReadRs

        assertEquals(MmTrackStub.get().toTransport(), response.track)
        assertEquals(1, response.errors?.size)
        assertEquals("err", response.errors?.firstOrNull()?.code)
        assertEquals("title", response.errors?.firstOrNull()?.field)
        assertEquals("wrong title", response.errors?.firstOrNull()?.message)
    }

    private fun MmTrack.toTransport(): TrackReadWithOwner =
        TrackReadWithOwner(
            id = this.id.asLong(),
            title = this.title,
            type = this.type.toTransport(),
            createDate = this.createDate,
            visibility = this.visibility.toTransport(),
            unit = this.unit,
            owner = this.owner.asString(),
        )
}
