package com.contract.management.domain.contract.repository

import com.contract.management.domain.contract.entity.Contract
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ContractRepository: JpaRepository<Contract, Long> {
    @Query(
        value = """
            SELECT * 
            FROM contract c 
            WHERE c.insurance_end_date = TIMESTAMPADD(DAY, 7, CURRENT_DATE)
        """,
        nativeQuery = true
    )
    fun findExpiringContractsOneWeekBefore(): List<Contract>

}