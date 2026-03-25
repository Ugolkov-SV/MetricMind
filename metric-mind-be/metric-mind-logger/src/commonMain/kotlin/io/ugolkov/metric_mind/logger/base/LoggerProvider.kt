package io.ugolkov.metric_mind.logger.base

import kotlin.reflect.KClass
import kotlin.reflect.KFunction

class LoggerProvider(
    private val provider: (String) -> ILogWrapper = { ILogWrapper.DEFAULT }
) {
    /**
     * Инициализирует и возвращает экземпляр логера
     */
    fun logger(loggerId: String): ILogWrapper = provider(loggerId)

    /**
     * Инициализирует и возвращает экземпляр логера
     */
    fun logger(clazz: KClass<*>): ILogWrapper = provider(clazz.qualifiedName ?: clazz.simpleName ?: "(unknown)")

    /**
     * Инициализирует и возвращает экземпляр логера
     */
    fun logger(function: KFunction<*>): ILogWrapper = provider(function.name)
}