package com.contract.management.domain.contractCoverage.service

import com.contract.management.domain.contractCoverage.entity.ContractCoverage
import com.contract.management.domain.contractCoverage.repository.ContractCoverageRepository
import com.contract.management.domain.coverage.service.CoverageService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ContractCoverageService(
    private val coverageService: CoverageService,
    private val contractCoverageRepository: ContractCoverageRepository
) {

    fun addContractCoverage(
        addCoverageIds: List<Long>,
        contractId: Long,
        productId: Long
    ) {
        val addContractCoverages = addCoverageIds.map { ContractCoverage.of(contractId, it) }
        coverageService.validateIncludedInProduct(addCoverageIds, productId)
        contractCoverageRepository.saveAll(addContractCoverages)
    }

    fun deleteContractCoverage(
        deleteCoverageIds: List<Long>,
        contractId: Long,
        productId: Long
    ) {
        val contractCoverages = contractCoverageRepository.findByContractId(contractId)
        coverageService.validateIncludedInProduct(deleteCoverageIds, productId)
        contractCoverages.filter { deleteCoverageIds.contains(it.coverageId) }
            .forEach { it.delete() }
    }
}