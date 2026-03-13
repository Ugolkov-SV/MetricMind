package io.ugolkov.metric_mind.spring.controller

import io.ugolkov.api.v1.models.*
import io.ugolkov.metric_mind.api.v1.mappers.fromTransport
import io.ugolkov.metric_mind.api.v1.mappers.toTransport
import io.ugolkov.metric_mind.spring.common.controllerTrackHelper
import io.ugolkov.metric_mind.spring.config.MmAppSettings
import org.springframework.web.bind.annotation.*
import kotlin.reflect.KClass

@Suppress("unused")
@RestController
@RequestMapping("v1/track")
class TrackControllerV1(private val appSettings: MmAppSettings) {
    @PostMapping("create")
    suspend fun create(@RequestBody request: TrackCreateRq): TrackCreateRs =
        process(appSettings, request = request, this::class, "create")

    @PostMapping("read")
    suspend fun read(@RequestBody request: TrackReadRq): TrackReadRs =
        process(appSettings, request = request, this::class, "read")

    @RequestMapping("update", method = [RequestMethod.POST])
    suspend fun update(@RequestBody request: TrackUpdateRq): TrackUpdateRs =
        process(appSettings, request = request, this::class, "update")

    @PostMapping("delete")
    suspend fun delete(@RequestBody request: TrackDeleteRq): TrackDeleteRs =
        process(appSettings, request = request, this::class, "delete")

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
            appSettings.controllerTrackHelper(
                getRequest = { fromTransport(request) },
                toResponse = { toTransport() as R },
                clazz = clazz,
                logId = logId,
            )
    }
}