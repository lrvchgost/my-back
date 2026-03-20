package ru.otus.otuskotlin.lrvch.logging.jvm

import ch.qos.logback.classic.Logger
import org.slf4j.LoggerFactory
import ru.otus.otuskotlin.lrvch.logging.common.IUniformLogWrapper
import kotlin.reflect.KClass

/**
 * Generate internal MpLogContext logger
 *
 * @param logger Logback instance from [LoggerFactory.getLogger()]
 */
fun catalogLoggerLogback(logger: Logger): IUniformLogWrapper = MpLogWrapperLogback(
    logger = logger,
    loggerId = logger.name,
)

fun catalogLoggerLogback(clazz: KClass<*>): IUniformLogWrapper = catalogLoggerLogback(LoggerFactory.getLogger(clazz.java) as Logger)
@Suppress("unused")
fun catalogLoggerLogback(loggerId: String): IUniformLogWrapper = catalogLoggerLogback(LoggerFactory.getLogger(loggerId) as Logger)
