package com.contract.management.domain.contract.repository

import com.contract.management.domain.contract.entity.Contract
import org.springframework.data.jpa.repository.JpaRepository

interface ContractRepository: JpaRepository<Contract, Long> {
}