package com.contract.management.domain.coverage.service

import com.contract.management.domain.coverage.controller.request.CoverageSaveRequest
import com.contract.management.domain.coverage.repository.CoverageRepository
import com.contract.management.domain.product.entity.Product
import com.contract.management.domain.product.repository.ProductRepository
import com.contract.management.factory.ProductFactory
import com.contract.management.global.exception.BusinessException
import com.contract.management.global.exception.ResponseCode
import com.contract.management.template.IntegrationTestTemplate
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import java.math.BigDecimal

class CoverageServiceTest(
    @Autowired private val coverageService: CoverageService,
    @Autowired private val coverageRepository: CoverageRepository,
    @Autowired private val productRepository: ProductRepository
): IntegrationTestTemplate() {

    private lateinit var product: Product

    @BeforeEach
    fun setup() {
        product = ProductFactory.createProduct()
        productRepository.save(product)
    }

    @Nested
    inner class `담보 저장 함수는` {

        @Test
        fun `중복된 담보가 있으면 예외를 던진다`() {
            // given
            val request = CoverageSaveRequest(
                productId = product.id,
                name = "테스트 담보",
                insuredAmount = BigDecimal("1000000"),
                baseAmount = BigDecimal("100")
            )
            coverageService.saveCoverage(request)

            // when & then
            assertThatThrownBy { coverageService.saveCoverage(request) }
                .isExactlyInstanceOf(BusinessException::class.java)
                .hasMessage(ResponseCode.DUPLICATE_COVERAGE.message)
        }

        @Test
        fun `담보 저장에 성공한다`() {
            // given
            val request = CoverageSaveRequest(
                productId = product.id,
                name = "테스트 담보",
                insuredAmount = BigDecimal("1000000"),
                baseAmount = BigDecimal("100")
            )

            // when
            val coverageId = coverageService.saveCoverage(request)

            // then
            val coverage = coverageRepository.findByIdOrNull(coverageId)!!
            assertThat(product.id).isEqualTo(coverage.productId)
            assertThat(request.name).isEqualTo(coverage.name)
            assertThat(request.insuredAmount).isEqualTo(coverage.insuredAmount)
            assertThat(request.baseAmount).isEqualTo(coverage.baseAmount)
        }
    }
}