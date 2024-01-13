package com.contract.management.factory

import com.contract.management.domain.contract.entity.Contract
import com.contract.management.domain.contract.entity.enums.ContractStatus
import java.math.BigDecimal
import java.time.YearMonth

class ContractFactory {

    companion object {
        fun createContract(
            productId: Long
        ): Contract = Contract(
            productId = productId,
            period = 12,
            totalAmount = BigDecimal("1000000"),
            insuranceStartDate = YearMonth.of(2023, 10),
            insuranceEndDate = YearMonth.of(2024, 6),
            status = ContractStatus.NORMAL
        )
    }
}