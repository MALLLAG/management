package com.contract.management.domain.product.entity

import com.contract.management.domain.product.controller.request.ProductSaveRequest
import com.contract.management.global.model.BaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.Comment
import org.hibernate.annotations.Where

@Entity
@Table(name = "product")
@Where(clause = "deleted_at is null")
class Product(
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Comment("상품 이름")
    @Column(name = "name")
    var name: String,

    @Comment("최소 계약 기간")
    @Column(name = "contract_period_minimum")
    var contractPeriodMinimum: Int,

    @Comment("최대 계약 기간")
    @Column(name = "contract_period_maximum")
    var contractPeriodMaximum: Int
): BaseEntity() {

    companion object {
        fun from(
            request: ProductSaveRequest
        ): Product = Product(
            name = request.name,
            contractPeriodMinimum = request.contractPeriodMinimum,
            contractPeriodMaximum = request.contractPeriodMaximum
        )
    }

}
