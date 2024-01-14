package com.contract.management.domain.coverage.controller.response

data class CoverageSaveResponse(
    val coverageId: Long
) {

    companion object {
        fun from(
            coverageId: Long
        ): CoverageSaveResponse = CoverageSaveResponse(
            coverageId = coverageId
        )
    }
}
