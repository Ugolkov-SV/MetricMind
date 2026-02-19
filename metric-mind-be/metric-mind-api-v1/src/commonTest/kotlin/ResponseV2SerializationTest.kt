package ru.otus.otuskotlin.marketplace.api.v2

import io.ugolkov.api.v1.models.IResponse
import io.ugolkov.api.v1.models.ResponseResult
import io.ugolkov.api.v1.models.TrackCreateRs
import io.ugolkov.api.v1.models.TrackId
import io.ugolkov.metric_mind.api.v1.apiV1Mapper
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseV2SerializationTest {
    private val response: IResponse =
        TrackCreateRs(
            result = ResponseResult.SUCCESS,
            track = TrackId(0L),
        )

    @Test
    fun serialize() {
        val json = apiV1Mapper.encodeToString(response)

        println(json)

        assertContains(json, Regex("\"id\":0"))
        assertContains(json, Regex("\"responseType\":\\s*\"createTrack\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.encodeToString(response)
        val obj = apiV1Mapper.decodeFromString<IResponse>(json) as TrackCreateRs

        assertEquals(response, obj)
    }
}
