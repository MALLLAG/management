package com.contract.management.domain.contract.entity

import com.contract.management.domain.contract.controller.request.ContractSaveRequest
import com.contract.management.domain.contract.entity.enums.ContractStatus
import com.contract.management.global.model.BaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.Comment
import org.hibernate.annotations.Where
import java.math.BigDecimal
import java.time.YearMonth
import java.time.temporal.ChronoUnit

@Entity
@Table(name = "contract")
@Where(clause = "deleted_at is null")
class Contract(
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Comment("상품 id")
    @Column(name = "product_id")
    var productId: Long,

    @Comment("계약 기간")
    @Column(name = "period")
    var period: Int,

    @Comment("총 보험료")
    @Column(name = "total_amount", precision = 10, scale = 2)
    var totalAmount: BigDecimal,

    @Comment("보험 시작일")
    @Column(name = "insurance_start_date")
    var insuranceStartDate: YearMonth,

    @Comment("보험 종료일")
    @Column(name = "insurance_end_date")
    var insuranceEndDate: YearMonth,

    @Comment("계약 상태")
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    var status: ContractStatus
): BaseEntity() {

    companion object {
        fun of(
            request: ContractSaveRequest,
            calculateAmount: BigDecimal
        ): Contract = Contract(
            productId = request.productId,
            period = request.contractPeriod,
            totalAmount = calculateAmount,
            insuranceStartDate = request.insuranceStartDate,
            insuranceEndDate = request.insuranceEndDate,
            status = ContractStatus.NORMAL
        )
    }

    fun isExpired(): Boolean = this.status == ContractStatus.EXPIRED

    fun calculatePeriod(): Int = (ChronoUnit.MONTHS.between(insuranceStartDate, insuranceEndDate) + 1).toInt()

    fun updateStatus(
        status: ContractStatus
    ) {
        this.status = status
    }

    fun updateInsuranceEndDate(
        insuranceEndDate: YearMonth
    ) {
        this.insuranceEndDate = insuranceEndDate
    }

    fun updateTotalAmount(
        calculateAmount: BigDecimal
    ) {
        this.totalAmount = calculateAmount
    }
}