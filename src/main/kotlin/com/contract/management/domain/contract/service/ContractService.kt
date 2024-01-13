package com.contract.management.domain.contract.service

import com.contract.management.domain.contract.controller.request.ContractModifyRequest
import com.contract.management.domain.contract.controller.request.ContractSaveRequest
import com.contract.management.domain.contract.controller.response.AmountEstimateResponse
import com.contract.management.domain.contract.controller.response.ContractResponse
import com.contract.management.domain.contract.entity.Contract
import com.contract.management.domain.contract.entity.enums.ContractStatus
import com.contract.management.domain.contract.repository.ContractRepository
import com.contract.management.domain.contractCoverage.entity.ContractCoverage
import com.contract.management.domain.contractCoverage.repository.ContractCoverageRepository
import com.contract.management.domain.contractCoverage.service.ContractCoverageService
import com.contract.management.domain.coverage.controller.response.CoverageResponse
import com.contract.management.domain.coverage.repository.CoverageRepository
import com.contract.management.domain.coverage.service.CoverageService
import com.contract.management.domain.product.repository.ProductRepository
import com.contract.management.global.exception.BusinessException
import com.contract.management.global.exception.ResponseCode
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.YearMonth

@Service
@Transactional
class ContractService(
    private val coverageService: CoverageService,
    private val contractCoverageService: ContractCoverageService,
    private val contractRepository: ContractRepository,
    private val productRepository: ProductRepository,
    private val coverageRepository: CoverageRepository,
    private val contractCoverageRepository: ContractCoverageRepository
) {

    @Transactional(readOnly = true)
    fun getContract(
        contractId: Long
    ): ContractResponse {
        val contract = contractRepository.findByIdOrNull(contractId) ?: throw BusinessException(ResponseCode.NOT_FOUNT_CONTRACT)
        val product = productRepository.findByIdOrNull(contract.productId) ?: throw BusinessException(ResponseCode.NOT_FOUND_PRODUCT)
        val coverageResponses = contractCoverageRepository.findByContractId(contract.id)
            .map { coverageRepository.findByIdOrNull(it.coverageId) ?: throw BusinessException(ResponseCode.NOT_FOUND_COVERAGE) }
            .map { CoverageResponse.from(it) }

        return ContractResponse.of(product.name, contract.period, coverageResponses)
    }

    @Transactional(readOnly = true)
    fun estimateAmount(
        productId: Long,
        contractPeriod: Int,
        coverageIds: List<Long>
    ): AmountEstimateResponse {
        val calculateAmount = coverageService.calculateAmount(productId, contractPeriod, coverageIds)

        return AmountEstimateResponse(calculateAmount)
    }

    fun saveContract(
        request: ContractSaveRequest
    ): Long {
        val calculateAmount = coverageService.calculateAmount(request.productId, request.contractPeriod, request.coverageIds)
        val contract = contractRepository.save(Contract.of(request, calculateAmount))
        val contractCoverages = request.coverageIds.map { ContractCoverage.of(contract.id, it) }
        contractCoverageRepository.saveAll(contractCoverages)

        return contract.id
    }

    fun modifyContract(
        request: ContractModifyRequest
    ): Long {
        val contract = contractRepository.findByIdOrNull(request.contractId) ?: throw BusinessException(ResponseCode.NOT_FOUNT_CONTRACT)
        validateContractStatus(contract)
        contractCoverageService.addContractCoverage(request.addCoverageIds, contract.id, contract.productId)
        contractCoverageService.deleteContractCoverage(request.deleteCoverageIds, contract.id, contract.productId)
        updateInsuranceEndDate(request, contract)
        updateContractStatus(request, contract)
        updateTotalAmount(contract)

        return contract.id
    }

    private fun validateContractStatus(
        contract: Contract
    ) {
        if (contract.isExpired()) {
            throw BusinessException(ResponseCode.ALREADY_EXPIRED)
        }
    }

    private fun updateInsuranceEndDate(
        request: ContractModifyRequest,
        contract: Contract
    ) {
        if (request.insuranceEndDate <= YearMonth.now()) {
            throw BusinessException(ResponseCode.INVALID_INSURANCE_END_DATE)
        }

        contract.updateInsuranceEndDate(request.insuranceEndDate)
    }

    private fun updateContractStatus(
        request: ContractModifyRequest,
        contract: Contract
    ) {
        val contractStatus = ContractStatus.from(request.contractStatus)

        contract.updateStatus(contractStatus)
    }

    private fun updateTotalAmount(
        contract: Contract
    ) {
        val coverageIds = contractCoverageRepository.findByContractId(contract.id)
            .filter { !it.isDelete() }
            .map { it.coverageId }
        val calculateAmount = coverageService.calculateAmount(contract.productId, contract.calculatePeriod(), coverageIds)

        contract.updateTotalAmount(calculateAmount)
    }

}