package io.ugolkov.metric_mind.spring.common

import io.ugolkov.metric_mind.common.MmContext
import io.ugolkov.metric_mind.common.model.MmState
import io.ugolkov.metric_mind.spring.config.MmAppSettings
import io.ugolkov.metric_mind.spring.helper.asMmError
import io.ugolkov.metric_mind.spring.logger.toLog
import kotlin.reflect.KClass
import kotlin.time.Clock

suspend inline fun <T> MmAppSettings.controllerTrackHelper(
    crossinline getRequest: suspend MmContext.() -> Unit,
    crossinline toResponse: suspend MmContext.() -> T,
    clazz: KClass<*>,
    logId: String,
): T =
    controllerHelper(
        processor = trackProcessor,
        getRequest = getRequest,
        toResponse = toResponse,
        clazz = clazz,
        logId = logId,
    )

suspend inline fun <T> MmAppSettings.controllerTrackRecordHelper(
    crossinline getRequest: suspend MmContext.() -> Unit,
    crossinline toResponse: suspend MmContext.() -> T,
    clazz: KClass<*>,
    logId: String,
): T =
    controllerHelper(
        processor = trackRecordProcessor,
        getRequest = getRequest,
        toResponse = toResponse,
        clazz = clazz,
        logId = logId,
    )

suspend inline fun <T> MmAppSettings.controllerHelper(
    processor: MmProcessor,
    crossinline getRequest: suspend MmContext.() -> Unit,
    crossinline toResponse: suspend MmContext.() -> T,
    clazz: KClass<*>,
    logId: String,
): T {
    val logger = corSettings.loggerProvider.logger(clazz)
    val ctx = MmContext(
        timeStart = Clock.System.now(),
    )
    return try {
        ctx.getRequest()
        logger.info(
            msg = "Request $logId started for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        processor.exec(ctx)
        logger.info(
            msg = "Request $logId processed for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        ctx.toResponse()
    } catch (e: Throwable) {
        logger.error(
            msg = "Request $logId failed for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        ctx.state = MmState.FAILING
        ctx.errors.add(e.asMmError())
        processor.exec(ctx)
        ctx.toResponse()
    }
}
