package com.contract.management.domain.product.controller.response

data class ProductSaveResponse(
    val productId: Long
) {

    companion object {
        fun from(
            productId: Long
        ): ProductSaveResponse = ProductSaveResponse(
            productId = productId
        )
    }
}