package ru.tuev.catalog.common.controller

import org.slf4j.Logger
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import kotlin.reflect.KClass

/**
 * методы, упрощающие обработку ошибок в контроллерах
 */
//TODO comments methods and classes

fun <R> withFallbacks(fallbacks: List<Fallback<R>>, body: () -> R): R {
    return try {
        body()
    } catch (exc: Exception) {
        fallbacks.first { it.isSuit(exc) }
                .run(exc)
    }
}

fun <R> withFallback(fallback: Fallback<R>, body: () -> R): R {
    return withFallbacks(listOf(fallback), body)
}

fun <R> withDefaultFallback(logger: Logger, body: () -> R): R {
    return withFallback(defaultFallback(logger), body)
}

// ========= Fallbacks functions ==============

fun <R> defaultFallback(logger: Logger): Fallback<R> {
    //для непредвиденных ошибок не используем сообщение из исключения для безопасности
    return fallback(Exception::class.java, HttpStatus.INTERNAL_SERVER_ERROR, logger, "Internal server error")
}

fun <R> fallback(
        exceptionClass: KClass<out Throwable>,
        httpStatus: HttpStatus,
        logger: Logger,
        reasonMessage: String? = null
): SimpleFallback<R> {
    return fallback(exceptionClass.java, httpStatus, logger, reasonMessage)
}

fun <R> fallback(
        exceptionClass: Class<out Throwable>,
        httpStatus: HttpStatus,
        logger: Logger,
        reasonMessage: String? = null
): SimpleFallback<R> {
    return SimpleFallback(exceptionClass, logger, httpStatus, reasonMessage)
}

// =============== Fallback classes =====================

interface Fallback<R> {
    fun isSuit(exc: Throwable): Boolean
    fun run(exc: Throwable): R
}

class SimpleFallback<R>(
        private val exceptionClass: Class<out Throwable>,
        private val logger: Logger,
        private val httpStatus: HttpStatus,
        private val reasonMessage: String? = null
) : Fallback<R> {

    override fun isSuit(exc: Throwable): Boolean {
        return exceptionClass.isInstance(exc)
    }

    override fun run(exc: Throwable): R {
        val message = reasonMessage ?: exc.message
        if (httpStatus.value() > 500) {
            logger.error(message, exc)
        } else {
            logger.warn(message, exc)
        }
        throw ResponseStatusException(httpStatus, message)
    }

}