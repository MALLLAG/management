package com.contract.management.domain.coverage.service

import com.contract.management.domain.coverage.controller.request.CoverageSaveRequest
import com.contract.management.domain.coverage.controller.response.CoverageSaveResponse
import com.contract.management.domain.coverage.entity.Coverage
import com.contract.management.domain.coverage.entity.calculateAmount
import com.contract.management.domain.coverage.entity.isIncludedInProduct
import com.contract.management.domain.coverage.repository.CoverageRepository
import com.contract.management.global.exception.BusinessException
import com.contract.management.global.exception.ResponseCode
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Service
@Transactional
class CoverageService(
    private val coverageRepository: CoverageRepository
) {

    fun saveCoverage(
        request: CoverageSaveRequest
    ): CoverageSaveResponse {
        validateDuplicateCoverage(request)
        val coverage = coverageRepository.save(Coverage.from(request))

        return CoverageSaveResponse.from(coverage.id)
    }

    private fun validateDuplicateCoverage(
        request: CoverageSaveRequest
    ) {
        if (coverageRepository.existsByName(request.name)) {
            throw BusinessException(ResponseCode.DUPLICATE_COVERAGE)
        }
    }

    @Transactional(readOnly = true)
    fun calculateAmount(
        productId: Long,
        contractPeriod: Int,
        coverageIds: List<Long>
    ): BigDecimal {
        return coverageRepository.findAllById(coverageIds)
            .takeIf { it.isIncludedInProduct(productId) }
            ?.calculateAmount(contractPeriod)
            ?: throw BusinessException(ResponseCode.NOT_INCLUDED_IN_PRODUCT)
    }

    fun validateIncludedInProduct(
        coverageIds: List<Long>,
        productId: Long
    ) {
        if (coverageIds.isEmpty()) {
            return
        }

        val coverages = coverageRepository.findAllById(coverageIds)

        if (coverages.isEmpty()) {
            throw BusinessException(ResponseCode.NOT_FOUND_COVERAGE)
        }

        if (!coverages.isIncludedInProduct(productId)) {
            throw BusinessException(ResponseCode.NOT_INCLUDED_IN_PRODUCT)
        }
    }
}