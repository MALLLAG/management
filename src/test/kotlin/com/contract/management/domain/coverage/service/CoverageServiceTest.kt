package com.contract.management.domain.coverage.service

import com.contract.management.domain.coverage.controller.request.CoverageSaveRequest
import com.contract.management.domain.coverage.entity.Coverage
import com.contract.management.domain.coverage.repository.CoverageRepository
import com.contract.management.domain.product.entity.Product
import com.contract.management.domain.product.repository.ProductRepository
import com.contract.management.factory.CoverageFactory
import com.contract.management.factory.ProductFactory
import com.contract.management.global.exception.BusinessException
import com.contract.management.global.exception.ResponseCode
import com.contract.management.template.IntegrationTestTemplate
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import java.math.BigDecimal

class CoverageServiceTest(
    @Autowired private val coverageService: CoverageService,
    @Autowired private val coverageRepository: CoverageRepository,
    @Autowired private val productRepository: ProductRepository
): IntegrationTestTemplate() {

    private lateinit var product: Product
    private lateinit var coverage1: Coverage
    private lateinit var coverage2: Coverage

    @BeforeEach
    fun setup() {
        product = ProductFactory.createProduct()
        productRepository.save(product)

        coverage1 = CoverageFactory.createCoverage1(product.id)
        coverageRepository.save(coverage1)

        coverage2 = CoverageFactory.createCoverage1(product.id)
        coverageRepository.save(coverage2)
    }

    @Nested
    inner class `담보 저장 함수는` {

        @Test
        fun `중복된 담보가 있으면 예외를 던진다`() {
            // given
            val coverageSaveRequest = CoverageSaveRequest(
                productId = product.id,
                name = "테스트 담보",
                insuredAmount = BigDecimal("1000000"),
                baseAmount = BigDecimal("100")
            )
            coverageService.saveCoverage(coverageSaveRequest)

            // when & then
            assertThatThrownBy { coverageService.saveCoverage(coverageSaveRequest) }
                .isExactlyInstanceOf(BusinessException::class.java)
                .hasMessage(ResponseCode.DUPLICATE_COVERAGE.message)
        }

        @Test
        fun `담보 저장에 성공한다`() {
            // given
            val coverageSaveRequest = CoverageSaveRequest(
                productId = product.id,
                name = "테스트 담보",
                insuredAmount = BigDecimal("1000000"),
                baseAmount = BigDecimal("100")
            )

            // when
            val coverageSaveResponse = coverageService.saveCoverage(coverageSaveRequest)

            // then
            val coverage = coverageRepository.findByIdOrNull(coverageSaveResponse.coverageId)!!
            assertThat(product.id).isEqualTo(coverage.productId)
            assertThat(coverageSaveRequest.name).isEqualTo(coverage.name)
            assertThat(coverageSaveRequest.insuredAmount).isEqualTo(coverage.insuredAmount)
            assertThat(coverageSaveRequest.baseAmount).isEqualTo(coverage.baseAmount)
        }
    }

    @Nested
    inner class `validateIncludedInProduct 함수는` {

        @Test
        fun `담보가 해당 상품에 존재하지 않으면 예외를 던진다`() {
            // given
            val coverageIds = listOf(coverage1.id, coverage2.id)
            val productId = Long.MAX_VALUE

            // when & then
            assertThatThrownBy { coverageService.validateIncludedInProduct(coverageIds, productId) }
                .isExactlyInstanceOf(BusinessException::class.java)
                .hasMessage(ResponseCode.NOT_INCLUDED_IN_PRODUCT.message)
        }
    }
}