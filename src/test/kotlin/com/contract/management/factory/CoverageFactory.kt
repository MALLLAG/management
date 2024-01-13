package com.contract.management.factory

import com.contract.management.domain.coverage.entity.Coverage
import java.math.BigDecimal

class CoverageFactory {

    companion object {
        fun createCoverage1(
            productId: Long
        ): Coverage = Coverage(
            productId = productId,
            name = "테스트 담보 1",
            insuredAmount = BigDecimal("750000"),
            baseAmount = BigDecimal("38")
        )

        fun createCoverage2(
            productId: Long
        ): Coverage = Coverage(
            productId = productId,
            name = "테스트 담보 2",
            insuredAmount = BigDecimal("1570000"),
            baseAmount = BigDecimal("40")
        )
    }
}