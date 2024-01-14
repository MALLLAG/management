package com.contract.management.domain.product.service

import com.contract.management.domain.product.controller.request.ProductSaveRequest
import com.contract.management.domain.product.repository.ProductRepository
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
            val productSaveRequest = ProductSaveRequest(
                name = "테스트 보험",
                contractPeriodMinimum = 1,
                contractPeriodMaximum = 6
            )
            productService.saveProduct(productSaveRequest)

            // when & then
            assertThatThrownBy { productService.saveProduct(productSaveRequest) }
                .isExactlyInstanceOf(BusinessException::class.java)
                .hasMessage(ResponseCode.DUPLICATE_PRODUCT.message)
        }

        @Test
        fun `상품 저장에 성공한다`() {
            // given
            val productSaveRequest = ProductSaveRequest(
                name = "테스트 보험",
                contractPeriodMinimum = 1,
                contractPeriodMaximum = 6
            )

            // when
            val productSaveResponse = productService.saveProduct(productSaveRequest)

            // then
            val product = productRepository.findByIdOrNull(productSaveResponse.productId)!!

            assertThat(productSaveRequest.name).isEqualTo(product.name)
            assertThat(productSaveRequest.contractPeriodMinimum).isEqualTo(product.contractPeriodMinimum)
            assertThat(productSaveRequest.contractPeriodMaximum).isEqualTo(product.contractPeriodMaximum)
        }
    }


}