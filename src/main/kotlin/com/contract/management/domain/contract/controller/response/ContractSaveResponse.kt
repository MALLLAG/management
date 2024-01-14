package com.contract.management.domain.contract.controller.response

data class ContractSaveResponse(
    val contractId: Long
) {

    companion object {
        fun from(
            contractId: Long
        ): ContractSaveResponse = ContractSaveResponse(
            contractId = contractId
        )
    }
}
