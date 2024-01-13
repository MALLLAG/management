package com.contract.management.domain.contract.controller.request

import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class ContractSaveRequest(
    val productId: Long,
    val insuranceStartDate: LocalDate,
    val insuranceEndDate: LocalDate,
    val coverageIds: List<Long>
) {
    fun calculatePeriod(): Int = (ChronoUnit.MONTHS.between(this.insuranceStartDate, this.insuranceEndDate) + 1).toInt()
}
