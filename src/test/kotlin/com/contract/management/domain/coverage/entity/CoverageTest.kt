package com.contract.management.domain.coverage.entity

import com.contract.management.factory.CoverageFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class CoverageTest {

    @Nested
    inner class `coverage의 calculateAmount 함수는` {

        @Test
        fun `가입 기간을 받아, 총 보험료를 계산해 반환한다`() {
            // given
            val contractPeriod = 11
            val coverage1 = CoverageFactory.createCoverage1(Long.MAX_VALUE - 1)
            val coverage2 = CoverageFactory.createCoverage2(Long.MAX_VALUE - 2)
            val coverages = listOf(coverage1, coverage2)

            // when
            val calculateAmount = coverages.calculateAmount(contractPeriod)

            // then
            assertThat(calculateAmount).isEqualTo(BigDecimal("648855.24"))
        }
    }
}