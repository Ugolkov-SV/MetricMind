package io.ugolkov.metric_mind.spring.controller

import io.ugolkov.api.v1.models.*
import io.ugolkov.metric_mind.api.v1.mappers.fromTransport
import io.ugolkov.metric_mind.api.v1.mappers.toTransport
import io.ugolkov.metric_mind.spring.common.controllerTrackRecordHelper
import io.ugolkov.metric_mind.spring.config.MmAppSettings
import org.springframework.web.bind.annotation.*
import kotlin.reflect.KClass

@Suppress("unused")
@RestController
@RequestMapping("v1/trackRecord")
class TrackRecordControllerV1(private val appSettings: MmAppSettings) {
    @PostMapping("create")
    suspend fun create(@RequestBody request: TrackRecordCreateRq): TrackRecordCreateRs =
        process(appSettings, request = request, this::class, "create")

    @PostMapping("read")
    suspend fun read(@RequestBody request: TrackRecordReadRq): TrackRecordReadRs =
        process(appSettings, request = request, this::class, "read")

    @RequestMapping("update", method = [RequestMethod.POST])
    suspend fun update(@RequestBody request: TrackRecordUpdateRq): TrackRecordUpdateRs =
        process(appSettings, request = request, this::class, "update")

    @PostMapping("delete")
    suspend fun delete(@RequestBody request: TrackRecordDeleteRq): TrackRecordDeleteRs =
        process(appSettings, request = request, this::class, "delete")

    companion object {
        suspend inline fun <reified Q : IRequest, reified R : IResponse> process(
            appSettings: MmAppSettings,
            request: Q,
            clazz: KClass<*>,
            logId: String,
        ): R =
            appSettings.controllerTrackRecordHelper(
                getRequest = { fromTransport(request) },
                toResponse = { toTransport() as R },
                clazz = clazz,
                logId = logId,
            )
    }
}