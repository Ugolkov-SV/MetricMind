package io.ugolkov.metric_mind.spring.controller

import io.ugolkov.api.v1.models.IResponse
import io.ugolkov.api.v1.models.TracksReadRs
import io.ugolkov.metric_mind.api.v1.mappers.toTransport
import io.ugolkov.metric_mind.biz.helper.controllerHelper
import io.ugolkov.metric_mind.spring.config.MmAppSettings
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.reflect.KClass

@Suppress("unused")
@RestController
@RequestMapping("v1/tracks")
class TracksControllerV1(private val appSettings: MmAppSettings) {
    @PostMapping("read")
    suspend fun read(@RequestHeader("Owner-ID") ownerId: String): TracksReadRs =
        process(appSettings, this::class, "read")

    companion object {
        suspend inline fun <reified R : IResponse> process(
            appSettings: MmAppSettings,
            clazz: KClass<*>,
            logId: String,
        ): R =
            appSettings.controllerHelper(
                getRequest = { },
                toResponse = { toTransport() as R },
                clazz = clazz,
                logId = logId,
            )
    }
}