package com.contract.management.domain.product.service

import com.contract.management.domain.product.controller.request.ProductSaveRequest
import com.contract.management.domain.product.entity.Product
import com.contract.management.domain.product.repository.ProductRepository
import com.contract.management.global.exception.BusinessException
import com.contract.management.global.exception.ResponseCode
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ProductService(
    private val productRepository: ProductRepository
) {

    fun saveProduct(
        request: ProductSaveRequest
    ): Long {
        validateDuplicateProduct(request)

        val product = productRepository.save(Product.from(request))
        return product.id
    }

    private fun validateDuplicateProduct(
        request: ProductSaveRequest
    ) {
        if (productRepository.existsByName(request.name)) {
            throw BusinessException(ResponseCode.DUPLICATE_PRODUCT)
        }
    }
}