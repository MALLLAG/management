package com.contract.management.domain.contract.controller

import com.contract.management.domain.contract.controller.request.ContractModifyRequest
import com.contract.management.domain.contract.controller.request.ContractSaveRequest
import com.contract.management.domain.contract.controller.response.AmountEstimateResponse
import com.contract.management.domain.contract.controller.response.ContractModifyResponse
import com.contract.management.domain.contract.controller.response.ContractResponse
import com.contract.management.domain.contract.controller.response.ContractSaveResponse
import com.contract.management.domain.contract.service.ContractService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/contracts")
class ContractController(
    private val contractService: ContractService
) {

    @GetMapping("/{contractId}")
    fun getContract(
        @PathVariable("contractId") contractId: Long
    ): ResponseEntity<ContractResponse> {
        return ResponseEntity(contractService.getContract(contractId), HttpStatus.OK)
    }

    @GetMapping("/estimate")
    fun estimateAmount(
        @RequestParam("productId") productId: Long,
        @RequestParam("contractPeriod") contractPeriod: Int,
        @RequestParam("coverageIds") coverageIds: List<Long>
    ): ResponseEntity<AmountEstimateResponse> {
        return ResponseEntity(contractService.estimateAmount(productId, contractPeriod, coverageIds), HttpStatus.OK)
    }

    @PostMapping
    fun saveContract(
        @Valid @RequestBody request: ContractSaveRequest
    ): ResponseEntity<ContractSaveResponse> {
        return ResponseEntity(contractService.saveContract(request), HttpStatus.CREATED)
    }

    @PatchMapping
    fun modifyContract(
        @Valid @RequestBody request: ContractModifyRequest
    ): ResponseEntity<ContractModifyResponse> {
        return ResponseEntity(contractService.modifyContract(request), HttpStatus.OK)
    }
}