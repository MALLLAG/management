package com.contract.management.global.exception

import org.apache.commons.lang3.exception.ExceptionUtils
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    companion object {
        private const val EXCEPTION_MESSAGE = "알 수 없는 에러가 발생하였습니다."
        private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)
    }

    @ExceptionHandler(Exception::class)
    fun exception(
        e: Exception
    ): ResponseEntity<String> {
        logger.error(ExceptionUtils.getStackTrace(e))
        return ResponseEntity(EXCEPTION_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(BusinessException::class)
    fun businessException(
        e: BusinessException
    ): ResponseEntity<String> {
        logger.error(ExceptionUtils.getStackTrace(e))
        return ResponseEntity(e.code.message, e.code.status)
    }
}