package com.contract.management.domain.product.controller.request

import com.contract.management.global.exception.ResponseCode
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive

data class ProductSaveRequest(
    @field:NotBlank(message = "등록하려는 상품명을 입력해주세요.")
    val name: String,
    @field:Positive(message = "최소 계약 기간은 0보다 큰 값을 입력해주세요.")
    val contractPeriodMinimum: Int,
    @field:Positive(message = "최대 계약 기간은 0보다 큰 값을 입력해주세요.")
    val contractPeriodMaximum: Int
) {
    init {
        checkContractPeriod()
    }

    private fun checkContractPeriod() {
        check(contractPeriodMaximum > contractPeriodMinimum) { ResponseCode.INVALID_CONTRACT_PERIOD.message }
    }
}
