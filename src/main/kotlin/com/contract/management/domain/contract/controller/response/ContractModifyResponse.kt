package com.contract.management.domain.contract.controller.response

data class ContractModifyResponse(
    val contractId: Long
) {

    companion object {
        fun from(
            contractId: Long
        ): ContractModifyResponse = ContractModifyResponse(
            contractId = contractId
        )
    }
}
