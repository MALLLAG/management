package com.contract.management.domain.contract.entity.enums

enum class ContractStatus {
    NORMAL,
    WITHDRAW,
    EXPIRED;

    companion object {
        fun from(
            status: String
        ): ContractStatus = ContractStatus.valueOf(status)
    }
}