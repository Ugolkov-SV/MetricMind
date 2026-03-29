package io.ugolkov.metric_mind.biz.helper

import io.ugolkov.metric_mind.biz.logger.toLog
import io.ugolkov.metric_mind.common.BaseContext
import io.ugolkov.metric_mind.common.IMmAppSettings
import io.ugolkov.metric_mind.common.createContext
import io.ugolkov.metric_mind.common.model.MmState
import kotlin.reflect.KClass
import kotlin.time.Clock

suspend inline fun <reified C : BaseContext, T> IMmAppSettings.controllerHelper(
    crossinline getRequest: suspend C.() -> Unit,
    crossinline toResponse: suspend C.() -> T,
    clazz: KClass<*>,
    logId: String,
): T {
    val logger = corSettings.loggerProvider.logger(clazz)
    val ctx = createContext<C>()
        .apply { timeStart = Clock.System.now() }

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
