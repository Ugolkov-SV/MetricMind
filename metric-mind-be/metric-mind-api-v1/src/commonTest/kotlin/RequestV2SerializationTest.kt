package io.ugolkov.metric_mind.api.v1

import io.ugolkov.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestV2SerializationTest {
    private val request: IRequest = TrackCreateRq(
        debug = Debug(
            mode = Mode.STUB,
            stub = Stubs.BAD_TITLE
        ),
        track = TrackWrite(
            title = "track title",
            type = TrackType.NUMBER,
            unit = "unit",
            visibility = Visibility.PRIVATE,
            createDate = 0,
        )
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.encodeToString(IRequest.serializer(), request)

        println(json)

        assertContains(json, Regex("\"title\":\\s*\"track title\""))
        assertContains(json, Regex("\"mode\":\\s*\"stub\""))
        assertContains(json, Regex("\"stub\":\\s*\"badTitle\""))
        assertContains(json, Regex("\"requestType\":\\s*\"createTrack\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.encodeToString(request)
        val obj = apiV1Mapper.decodeFromString<IRequest>(json) as TrackCreateRq

        assertEquals(request, obj)
    }
}
