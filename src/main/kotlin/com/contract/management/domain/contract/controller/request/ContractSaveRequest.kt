package com.contract.management.domain.contract.controller.request

import java.time.LocalDate

data class ContractSaveRequest(
    val productId: Long,
    val contractPeriod: Int,
    val insuranceStartDate: LocalDate,
    val insuranceEndDate: LocalDate,
    val coverageIds: List<Long>
)
