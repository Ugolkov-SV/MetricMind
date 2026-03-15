package io.ugolkov.metric_mind.spring.controller

import io.ugolkov.api.v1.models.*
import io.ugolkov.metric_mind.api.v1.mappers.*
import io.ugolkov.metric_mind.common.IProcessor
import io.ugolkov.metric_mind.common.MmContext
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

@WebFluxTest(TrackControllerV1::class, MetricMindConfig::class)
class TrackControllerV1Test {
    @Autowired
    private lateinit var webClient: WebTestClient

    @Suppress("unused")
    @MockitoBean
    private lateinit var processor: IProcessor

    @Test
    fun createTrack() =
        testStubTrack(
            url = "$TRACK_PATH/create",
            requestObj = TrackCreateRq(
                TrackWrite(
                    title = "track title",
                    type = TrackType.NUMBER,
                    unit = "unit",
                    visibility = Visibility.PRIVATE,
                    createDate = 0,
                )
            ),
            responseObj = MmContext().toTransportTrackCreate()
        )

    @Test
    fun readTrack() =
        testStubTrack(
            url = "$TRACK_PATH/read",
            requestObj = TrackReadRq(TrackId(id = 0)),
            responseObj = MmContext().toTransportTrackRead()
        )

    @Test
    fun updateTrack() =
        testStubTrack(
            url = "$TRACK_PATH/update",
            requestObj = TrackUpdateRq(
                TrackUpdateRqAllOfTrack(
                    id = 0L,
                    title = "track title",
                    unit = "unit",
                    visibility = Visibility.PRIVATE,
                )
            ),
            responseObj = MmContext().toTransportTrackUpdate()
        )

    @Test
    fun deleteTrack() =
        testStubTrack(
            url = "$TRACK_PATH/delete",
            requestObj = TrackDeleteRq(TrackId(id = 0)),
            responseObj = MmContext().toTransportTrackDelete()
        )

    @Test
    fun searchTrack() =
        testStubTrack(
            url = "$TRACK_PATH/search",
            requestObj = TrackSearchRq(TrackSearchRqAllOfFilter(searchTrack = "test")),
            responseObj = MmContext().toTransportTrackSearch()
        )

    private inline fun <reified Rq : IRequest, reified Rs : IResponse> testStubTrack(
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
        const val TRACK_PATH = "/v1/track"
    }
}