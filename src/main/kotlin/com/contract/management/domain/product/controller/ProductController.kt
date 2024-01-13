package com.contract.management.domain.product.controller

import com.contract.management.domain.product.controller.request.ProductSaveRequest
import com.contract.management.domain.product.service.ProductService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/products")
class ProductController(
    private val productService: ProductService
) {

    @PostMapping
    fun saveProduct(
        @Valid @RequestBody request: ProductSaveRequest
    ): ResponseEntity<Long> {
        return ResponseEntity(productService.saveProduct(request), HttpStatus.CREATED)
    }
}