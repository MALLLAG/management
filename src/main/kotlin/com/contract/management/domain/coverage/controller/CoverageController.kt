package com.contract.management.domain.coverage.controller

import com.contract.management.domain.coverage.controller.request.CoverageSaveRequest
import com.contract.management.domain.coverage.service.CoverageService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/coverages")
class CoverageController(
    private val coverageService: CoverageService
) {

    @PostMapping
    fun saveCoverage(
        @Valid @RequestBody request: CoverageSaveRequest
    ): ResponseEntity<Unit> {
        coverageService.saveCoverage(request)
        return ResponseEntity(HttpStatus.CREATED)
    }
}