package com.contract.management.domain.coverage.controller.response

import com.contract.management.domain.coverage.entity.Coverage
import java.math.BigDecimal

data class CoverageResponse(
    val coverageName: String,
    val insuredAmount: BigDecimal,
    val baseAmount: BigDecimal
) {

    companion object {
        fun from(
            coverage: Coverage
        ): CoverageResponse = CoverageResponse(
            coverageName = coverage.name,
            insuredAmount = coverage.insuredAmount,
            baseAmount = coverage.baseAmount
        )
    }
}
