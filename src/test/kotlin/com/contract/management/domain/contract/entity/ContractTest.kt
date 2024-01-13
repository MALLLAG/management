package com.contract.management.domain.contract.entity

import com.contract.management.factory.ContractFactory
import org.junit.jupiter.api.Assertions.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class ContractTest {

    @Nested
    class `getPeriod 함수는` {

        @Test
        fun `보험 종료일과 보험 시작일으로 계약 기간을 계산하여 반환한다`() {
            // given
            val contract = ContractFactory.createContract(Long.MAX_VALUE)

            // when
            val period = contract.calculatePeriod()

            // then
            assertThat(period).isEqualTo(9)
        }
    }
}