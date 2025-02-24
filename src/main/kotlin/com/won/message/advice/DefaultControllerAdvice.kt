package com.won.message.advice

import com.won.message.exception.MessageException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class DefaultControllerAdvice {

    @ExceptionHandler(MessageException.MessageNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleMessageNotFoundException(e: MessageException.MessageNotFoundException) {
    }

    @ExceptionHandler(Throwable::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleUnknownException(e: Throwable) {
    }

}
