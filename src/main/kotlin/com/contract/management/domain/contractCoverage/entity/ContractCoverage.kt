package com.contract.management.domain.contractCoverage.entity

import com.contract.management.global.model.BaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.Comment
import org.hibernate.annotations.Where

@Entity
@Table(name = "contract_coverage")
@Where(clause = "deleted_at is null")
class ContractCoverage(
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Comment("계약 id")
    @Column(name = "contract_id")
    var contractId: Long,

    @Comment("담보 id")
    @Column(name = "coverage_id")
    var coverageId: Long
): BaseEntity() {

    companion object {
        fun of(
            contractId: Long,
            coverageId: Long
        ): ContractCoverage = ContractCoverage(
            contractId = contractId,
            coverageId = coverageId
        )
    }
}