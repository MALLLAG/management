package com.contract.management.domain.coverage.repository

import com.contract.management.domain.coverage.entity.Coverage
import org.springframework.data.jpa.repository.JpaRepository

interface CoverageRepository: JpaRepository<Coverage, Long> {
    fun existsByName(name: String): Boolean
}