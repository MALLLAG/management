package com.contract.management.factory

import com.contract.management.domain.product.entity.Product

class ProductFactory {

    companion object {
        fun createProduct(): Product = Product(
            name = "테스트 보험",
            contractPeriodMinimum = 1,
            contractPeriodMaximum = 6
        )
    }
}