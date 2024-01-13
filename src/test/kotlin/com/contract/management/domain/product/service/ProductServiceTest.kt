package com.contract.management.domain.product.service

import com.contract.management.domain.product.controller.request.ProductSaveRequest
import com.contract.management.domain.product.repository.ProductRepository
import com.contract.management.factory.ProductFactory
import com.contract.management.global.exception.BusinessException
import com.contract.management.global.exception.ResponseCode
import com.contract.management.template.IntegrationTestTemplate
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull

class ProductServiceTest(
    @Autowired private val productService: ProductService,
    @Autowired private val productRepository: ProductRepository
): IntegrationTestTemplate() {

    @Nested
    inner class `상품 저장 함수는` {

        @Test
        fun `중복된 상품이 있으면 예외를 던진다`() {
            // given
            val request = ProductSaveRequest(
                name = "테스트 보험",
                contractPeriodMinimum = 1,
                contractPeriodMaximum = 6
            )
            productService.saveProduct(request)

            // when & then
            assertThatThrownBy { productService.saveProduct(request) }
                .isExactlyInstanceOf(BusinessException::class.java)
                .hasMessage(ResponseCode.DUPLICATE_PRODUCT.message)
        }

        @Test
        fun `상품 저장에 성공한다`() {
            // given
            val request = ProductSaveRequest(
                name = "테스트 보험",
                contractPeriodMinimum = 1,
                contractPeriodMaximum = 6
            )

            // when
            val productId = productService.saveProduct(request)

            // then
            val product = productRepository.findByIdOrNull(productId)!!

            assertThat(request.name).isEqualTo(product.name)
            assertThat(request.contractPeriodMinimum).isEqualTo(product.contractPeriodMinimum)
            assertThat(request.contractPeriodMaximum).isEqualTo(product.contractPeriodMaximum)
        }
    }


}