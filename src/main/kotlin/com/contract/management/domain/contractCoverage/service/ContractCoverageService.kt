package com.contract.management.domain.contractCoverage.service

import com.contract.management.domain.contractCoverage.entity.ContractCoverage
import com.contract.management.domain.contractCoverage.repository.ContractCoverageRepository
import com.contract.management.domain.coverage.service.CoverageService
import com.contract.management.global.exception.BusinessException
import com.contract.management.global.exception.ResponseCode
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
        validateDuplicateContractCoverage(addCoverageIds, contractId)
        val addContractCoverages = addCoverageIds.map { ContractCoverage.of(contractId, it) }
        coverageService.validateIncludedInProduct(addCoverageIds, productId)
        contractCoverageRepository.saveAll(addContractCoverages)
    }

    fun deleteContractCoverage(
        deleteCoverageIds: List<Long>,
        contractId: Long,
        productId: Long
    ) {
        val deleteContractCoverages = contractCoverageRepository.findByContractId(contractId)
        coverageService.validateIncludedInProduct(deleteCoverageIds, productId)
        deleteContractCoverages.filter { deleteCoverageIds.contains(it.coverageId) }
            .forEach { it.delete() }
    }

    private fun validateDuplicateContractCoverage(
        coverageIds: List<Long>,
        contractId: Long
    ) {
        if (coverageIds.any { isDuplicateContractCoverage(it, contractId) }) {
            throw BusinessException(ResponseCode.DUPLICATE_CONTRACT_COVERAGE)
        }
    }

    private fun isDuplicateContractCoverage(
        coverageId: Long,
        contractId: Long
    ): Boolean {
        val contractCoverage = contractCoverageRepository.findByContractIdAndCoverageId(contractId, coverageId)

        return contractCoverage != null
    }
}