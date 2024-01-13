package com.contract.management.domain.contractCoverage.repository

import com.contract.management.domain.contractCoverage.entity.ContractCoverage
import org.springframework.data.jpa.repository.JpaRepository

interface ContractCoverageRepository: JpaRepository<ContractCoverage, Long> {
    fun findByContractId(contractId: Long): List<ContractCoverage>
}