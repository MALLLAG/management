package com.contract.management.domain.contract.scheduler

import com.contract.management.domain.contract.service.ContractService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ContractScheduler(
    private val contractService: ContractService
) {

    @Scheduled(cron = "0 0 12 * * ?")
    fun sendMailForExpiringContractsOneWeekBefore() {
        contractService.findExpiringContractsOneWeekBefore()
            .forEach { println("계약 번호 : ${it.id}의 계약 만기 일주일 전입니다.") }
    }
}