package io.ugolkov.metric_mind.spring.controller

import io.ugolkov.api.v1.models.IRequest
import io.ugolkov.api.v1.models.IResponse
import io.ugolkov.api.v1.models.TrackSearchRq
import io.ugolkov.api.v1.models.TrackSearchRqAllOfFilter
import io.ugolkov.metric_mind.api.v1.mappers.toTransportTrackSearch
import io.ugolkov.metric_mind.common.IProcessor
import io.ugolkov.metric_mind.common.TrackFilterContext
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

@WebFluxTest(TrackFilterControllerV1::class, MetricMindConfig::class)
class TrackFilterControllerV1Test {
    @Autowired
    private lateinit var webClient: WebTestClient

    @Suppress("unused")
    @MockitoBean
    private lateinit var processor: IProcessor

    @Test
    fun searchTrack() =
        testStubTrack(
            url = "$TRACK_PATH/search",
            requestObj = TrackSearchRq(TrackSearchRqAllOfFilter(searchTrack = "test")),
            responseObj = TrackFilterContext().toTransportTrackSearch()
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