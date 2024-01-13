package com.contract.management.domain.contract.controller.request

import java.time.LocalDate

data class AmountEstimateRequest(
    val productId: Long,
    val contractPeriod: Int,
    val coverageIds: List<Long>
)
