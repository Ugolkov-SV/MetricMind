package io.ugolkov.metric_mind.spring.controller

import io.ugolkov.api.v1.models.*
import io.ugolkov.metric_mind.api.v1.mappers.toTransportTrackRecordCreate
import io.ugolkov.metric_mind.api.v1.mappers.toTransportTrackRecordDelete
import io.ugolkov.metric_mind.api.v1.mappers.toTransportTrackRecordRead
import io.ugolkov.metric_mind.api.v1.mappers.toTransportTrackRecordUpdate
import io.ugolkov.metric_mind.common.IProcessor
import io.ugolkov.metric_mind.common.TrackRecordContext
import io.ugolkov.metric_mind.spring.config.MetricMindConfig
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import org.springframework.web.reactive.function.BodyInserters
import kotlin.test.Test

@WebFluxTest(TrackRecordControllerV1::class, MetricMindConfig::class)
class TrackRecordControllerV1Test {
    @Autowired
    private lateinit var webClient: WebTestClient

    @Suppress("unused")
    @MockitoBean
    private lateinit var processor: IProcessor

    @Test
    fun createTrackRecord() =
        testStubTrackRecord(
            url = "$TRACK_RECORD_PATH/create",
            requestObj = TrackRecordCreateRq(
                TrackRecord(
                    trackRecordId = 1L,
                    trackId = 1L,
                    value = 10.0,
                    date = 0,
                )
            ),
            responseObj = TrackRecordContext().toTransportTrackRecordCreate()
        )

    @Test
    fun readTrackRecord() =
        testStubTrackRecord(
            url = "$TRACK_RECORD_PATH/read",
            requestObj = TrackRecordReadRq(TrackId(id = 0)),
            responseObj = TrackRecordContext().toTransportTrackRecordRead()
        )

    @Test
    fun updateTrackRecord() =
        testStubTrackRecord(
            url = "$TRACK_RECORD_PATH/update",
            requestObj = TrackRecordUpdateRq(
                TrackRecord(
                    trackRecordId = 1L,
                    trackId = 1L,
                    value = 10.0,
                    date = 0,
                )
            ),
            responseObj = TrackRecordContext().toTransportTrackRecordUpdate()
        )

    @Test
    fun deleteTrackRecord() =
        testStubTrackRecord(
            url = "$TRACK_RECORD_PATH/delete",
            requestObj = TrackRecordDeleteRq(
                TrackRecordDeleteRqAllOfTrackRecord(
                    trackRecordId = 1L,
                    date = 0L,
                )
            ),
            responseObj = TrackRecordContext().toTransportTrackRecordDelete()
        )

    private inline fun <reified Rq : IRequest, reified Rs : IResponse> testStubTrackRecord(
        url: String,
        requestObj: Rq,
        responseObj: Rs,
    ) {
        webClient.post()
            .uri(url)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(requestObj))
            .exchange()
            .expectStatus().isOk
            .expectBody<Rs>()
            .value {
                println("ACTUAL: $it")
                println("EXPECTED: $responseObj")
                assertThat(it).isEqualTo(responseObj)
            }
    }

    private companion object {
        const val TRACK_RECORD_PATH = "/v1/trackRecord"
    }
}