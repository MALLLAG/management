package com.contract.management.domain.contract.controller.request

import jakarta.validation.constraints.NotBlank
import java.time.LocalDate
import java.time.YearMonth

data class ContractModifyRequest(
    val contractId: Long,
    val addCoverageIds: List<Long>,
    val deleteCoverageIds: List<Long>,
    val insuranceEndDate: YearMonth,
    @field:NotBlank
    val contractStatus: String
)
