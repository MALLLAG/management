package com.contract.management.domain.coverage.controller.request

import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import java.math.BigDecimal

data class CoverageSaveRequest(
    @field:Positive(message = "등록하려는 담보의 상품을 지정해주세요.")
    val productId: Long,
    @field:NotBlank(message = "등록하려는 담보명을 입력해주세요.")
    val name: String,
    @field:DecimalMin(value = "0", message = "가입 금액은 0보다 커야합니다.")
    val insuredAmount: BigDecimal,
    @field:DecimalMin(value = "0", message = "기준 금액은 0보다 커야합니다.")
    val baseAmount: BigDecimal
)
