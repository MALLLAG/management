package com.contract.management.domain.contract.controller.request

import java.time.LocalDate
import java.time.YearMonth

data class ContractSaveRequest(
    val productId: Long,
    val contractPeriod: Int,
    val insuranceStartDate: YearMonth,
    val insuranceEndDate: YearMonth,
    val coverageIds: List<Long>
)
