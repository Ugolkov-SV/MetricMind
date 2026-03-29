package io.ugolkov.metric_mind.spring.controller

import io.ugolkov.api.v1.models.IRequest
import io.ugolkov.api.v1.models.IResponse
import io.ugolkov.api.v1.models.TrackSearchRq
import io.ugolkov.api.v1.models.TrackSearchRs
import io.ugolkov.metric_mind.api.v1.mappers.fromTransport
import io.ugolkov.metric_mind.api.v1.mappers.toTransport
import io.ugolkov.metric_mind.biz.helper.controllerHelper
import io.ugolkov.metric_mind.common.TrackFilterContext
import io.ugolkov.metric_mind.spring.config.MmAppSettings
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.reflect.KClass

@Suppress("unused")
@RestController
@RequestMapping("v1/track")
class TrackFilterControllerV1(private val appSettings: MmAppSettings) {
    @PostMapping("search")
    suspend fun search(@RequestBody request: TrackSearchRq): TrackSearchRs =
        process(appSettings, request = request, this::class, "search")

    companion object {
        suspend inline fun <reified Q : IRequest, reified R : IResponse> process(
            appSettings: MmAppSettings,
            request: Q,
            clazz: KClass<*>,
            logId: String,
        ): R =
            appSettings.controllerHelper<TrackFilterContext, R>(
                getRequest = { fromTransport(request) },
                toResponse = { toTransport() as R },
                clazz = clazz,
                logId = logId,
            )
    }
}