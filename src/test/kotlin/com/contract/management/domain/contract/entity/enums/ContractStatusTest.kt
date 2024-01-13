package com.contract.management.domain.contract.entity.enums

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class ContractStatusTest {

    @Nested
    class `ContractStatus의 from 함수는` {

        @Test
        fun `String의 status 값을 받아, enum으로 변환해서 반환한다`() {
            // given
            val status = "NORMAL"

            // when
            val contractStatus = ContractStatus.from(status)

            // then
            assertThat(contractStatus).isEqualTo(ContractStatus.NORMAL)
        }
    }
}