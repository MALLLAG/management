package com.contract.management.global.exception

class BusinessException(
    val code: ResponseCode,
): RuntimeException(code.message)
