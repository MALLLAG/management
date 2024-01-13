package com.contract.management.factory

import com.contract.management.domain.contract.entity.Contract
import com.contract.management.domain.contract.entity.enums.ContractStatus
import java.math.BigDecimal
import java.time.LocalDate

class ContractFactory {

    companion object {
        fun createContract(
            productId: Long
        ): Contract = Contract(
            productId = productId,
            period = 12,
            totalAmount = BigDecimal("1000000"),
            insuranceStartDate = LocalDate.of(2023, 10, 1),
            insuranceEndDate = LocalDate.of(2024, 6, 1),
            status = ContractStatus.NORMAL
        )
    }
}