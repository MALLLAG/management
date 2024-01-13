package com.contract.management.domain.coverage.entity

import com.contract.management.domain.coverage.controller.request.CoverageSaveRequest
import com.contract.management.global.model.BaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.Comment
import org.hibernate.annotations.Where
import java.math.BigDecimal
import java.math.RoundingMode

@Entity
@Table(name = "coverage")
@Where(clause = "deleted_at is null")
class Coverage(
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Comment("상품 id")
    @Column(name = "product_id")
    var productId: Long,

    @Comment("담보명")
    @Column(name = "name")
    var name: String,

    @Comment("가입 금액")
    @Column(name = "insured_amount", precision = 10, scale = 0)
    var insuredAmount: BigDecimal,

    @Comment("기준 금액")
    @Column(name = "base_amount", precision = 10, scale = 0)
    var baseAmount: BigDecimal
): BaseEntity() {

    companion object {
        fun from(
            request: CoverageSaveRequest
        ): Coverage = Coverage(
            productId = request.productId,
            name = request.name,
            insuredAmount = request.insuredAmount,
            baseAmount = request.baseAmount
        )
    }
}

fun List<Coverage>.isIncludedInProduct(productId: Long): Boolean =
    all { it.productId == productId }

fun List<Coverage>.calculateAmount(
    contractPeriod: Int
): BigDecimal = this.sumOf { it.calculate() }  * BigDecimal(contractPeriod)

private fun Coverage.calculate(): BigDecimal = this.insuredAmount
    .divide(this.baseAmount, 2, RoundingMode.FLOOR)
