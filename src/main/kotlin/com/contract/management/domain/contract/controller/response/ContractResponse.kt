package com.contract.management.domain.contract.controller.response

import com.contract.management.domain.contract.entity.Contract
import com.contract.management.domain.contract.entity.enums.ContractStatus
import com.contract.management.domain.coverage.controller.response.CoverageResponse
import java.math.BigDecimal
import java.time.LocalDate

data class ContractResponse(
    val productName: String,
    val contractPeriod: Int,
    val totalAmount: BigDecimal,
    val insuranceStartDate: LocalDate,
    val insuranceEndDate: LocalDate,
    val contractStatus: ContractStatus,
    val coverages: List<CoverageResponse>
) {

    companion object {
        fun of(
            productName: String,
            contract: Contract,
            coverages: List<CoverageResponse>
        ): ContractResponse = ContractResponse(
            productName = productName,
            contractPeriod = contract.calculatePeriod(),
            totalAmount = contract.totalAmount,
            insuranceStartDate = contract.insuranceStartDate,
            insuranceEndDate = contract.insuranceEndDate,
            contractStatus = contract.status,
            coverages = coverages
        )
    }
}
