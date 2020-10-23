package ru.tuev.catalog.common.controller

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import javax.validation.ConstraintViolationException

/**
 * Заменяет 500 ошибки на 400 при невалидных данных
 */

@ControllerAdvice
class ConstraintViolationExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [ConstraintViolationException::class])
    protected fun handleConstraintViolation(
            e: ConstraintViolationException,
            request: WebRequest?
    ): ResponseEntity<Any> {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message, e)
    }
}