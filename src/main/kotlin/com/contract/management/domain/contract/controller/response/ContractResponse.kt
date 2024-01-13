package com.contract.management.domain.contract.controller.response

import com.contract.management.domain.coverage.controller.response.CoverageResponse

data class ContractResponse(
    val productName: String,
    val contractPeriod: Int,
    val coverages: List<CoverageResponse>
) {

    companion object {
        fun of(
            productName: String,
            contractPeriod: Int,
            coverages: List<CoverageResponse>
        ): ContractResponse = ContractResponse(
            productName = productName,
            contractPeriod = contractPeriod,
            coverages = coverages
        )
    }
}
