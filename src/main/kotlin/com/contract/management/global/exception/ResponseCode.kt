package com.contract.management.global.exception

import org.springframework.http.HttpStatus

enum class ResponseCode(
    val message: String,
    val status: HttpStatus
) {
    // contract
    NOT_FOUNT_CONTRACT("해당 계약 정보를 찾지 못했습니다.", HttpStatus.BAD_REQUEST),
    ALREADY_EXPIRED("이미 만료된 계약입니다.", HttpStatus.BAD_REQUEST),
    INVALID_INSURANCE_END_DATE("해당 기간으로는 계약 기간 변경이 불가능합니다.", HttpStatus.BAD_REQUEST),

    // contract coverage
    DUPLICATE_CONTRACT_COVERAGE("이미 계약된 담보입니다.", HttpStatus.BAD_REQUEST),

    // coverage
    NOT_FOUND_COVERAGE("해당 담보 정보를 찾지 못했습니다.", HttpStatus.BAD_REQUEST),
    DUPLICATE_COVERAGE("이미 등록된 담보입니다.", HttpStatus.BAD_REQUEST),
    NOT_INCLUDED_IN_PRODUCT("요청하신 담보는 해당 상품에 존재하지 않습니다.", HttpStatus.BAD_REQUEST),

    // product
    NOT_FOUND_PRODUCT("해당 상품 정보를 찾지 못했습니다.", HttpStatus.BAD_REQUEST),
    INVALID_CONTRACT_PERIOD("최대 계약 기간은 최소 계약 기간보다 커야 합니다.", HttpStatus.BAD_REQUEST),
    DUPLICATE_PRODUCT("이미 등록된 상품입니다.", HttpStatus.BAD_REQUEST)
}