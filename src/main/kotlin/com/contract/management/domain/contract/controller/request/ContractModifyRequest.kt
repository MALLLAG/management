package com.contract.management.domain.contract.controller.request

import jakarta.validation.constraints.NotBlank
import java.time.LocalDate

data class ContractModifyRequest(
    val contractId: Long,
    val addCoverageIds: List<Long>,
    val deleteCoverageIds: List<Long>,
    val insuranceEndDate: LocalDate,
    @field:NotBlank
    val contractStatus: String
)
