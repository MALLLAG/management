package com.contract.management.domain.product.repository

import com.contract.management.domain.product.entity.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository: JpaRepository<Product, Long> {
    fun existsByName(name: String): Boolean
}