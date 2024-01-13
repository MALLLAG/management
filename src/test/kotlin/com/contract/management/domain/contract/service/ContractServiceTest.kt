package com.contract.management.domain.contract.service

import com.contract.management.domain.contract.controller.request.ContractModifyRequest
import com.contract.management.domain.contract.controller.request.ContractSaveRequest
import com.contract.management.domain.contract.entity.enums.ContractStatus
import com.contract.management.domain.contract.repository.ContractRepository
import com.contract.management.domain.contractCoverage.repository.ContractCoverageRepository
import com.contract.management.domain.coverage.entity.Coverage
import com.contract.management.domain.coverage.repository.CoverageRepository
import com.contract.management.domain.coverage.service.CoverageService
import com.contract.management.domain.product.entity.Product
import com.contract.management.domain.product.repository.ProductRepository
import com.contract.management.factory.CoverageFactory
import com.contract.management.factory.ProductFactory
import com.contract.management.global.exception.BusinessException
import com.contract.management.global.exception.ResponseCode
import com.contract.management.template.IntegrationTestTemplate
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.annotation.Rollback
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

class ContractServiceTest(
    @Autowired private val contractService: ContractService,
    @Autowired private val coverageService: CoverageService,
    @Autowired private val contractRepository: ContractRepository,
    @Autowired private val coverageRepository: CoverageRepository,
    @Autowired private val productRepository: ProductRepository,
    @Autowired private val contractCoverageRepository: ContractCoverageRepository
) : IntegrationTestTemplate() {

    private lateinit var product: Product
    private lateinit var coverage1: Coverage
    private lateinit var coverage2: Coverage

    @BeforeEach
    fun setup() {
        product = ProductFactory.createProduct()
        productRepository.save(product)

        coverage1 = CoverageFactory.createCoverage1(product.id)
        coverageRepository.save(coverage1)

        coverage2 = CoverageFactory.createCoverage1(product.id)
        coverageRepository.save(coverage2)
    }

    @Nested
    inner class `계약 조회 함수는` {

        @Test
        fun `계약 정보 조회에 성공한다`() {
            // given
            val saveRequest = ContractSaveRequest(
                productId = product.id,
                insuranceStartDate = LocalDate.now(),
                insuranceEndDate = LocalDate.now().plusMonths(1),
                coverageIds = listOf(coverage1.id)
            )
            val contractId = contractService.saveContract(saveRequest)

            // when
            val contractResponse = contractService.getContract(contractId)

            // then
            assertThat(product.name).isEqualTo(contractResponse.productName)
            assertThat(saveRequest.calculatePeriod()).isEqualTo(contractResponse.contractPeriod)
            assertThat(coverage1.name).isEqualTo(contractResponse.coverages[0].coverageName)
            assertThat(contractResponse.coverages[0].baseAmount).isNotNull
            assertThat(contractResponse.coverages[0].insuredAmount).isNotNull
        }
    }

    @Nested
    inner class `계약 생성 함수는` {

        @Test
        fun `계약 정보 저장에 성공한다`() {
            // given
            val request = ContractSaveRequest(
                productId = product.id,
                insuranceStartDate = LocalDate.now(),
                insuranceEndDate = LocalDate.now().plusMonths(1),
                coverageIds = listOf(coverage1.id)
            )

            // when
            val contractId = contractService.saveContract(request)

            // then
            val contract = contractRepository.findByIdOrNull(contractId)!!
            val contractCoverages = contractCoverageRepository.findByContractId(contractId)
            val coverages = contractCoverages.map {
                coverageRepository.findByIdOrNull(it.coverageId)
                    ?: throw BusinessException(ResponseCode.NOT_FOUND_COVERAGE)
            }

            assertThat(request.productId).isEqualTo(contract.productId)
            assertThat(request.calculatePeriod()).isEqualTo(contract.calculatePeriod())
            assertThat(request.insuranceStartDate).isEqualTo(contract.insuranceStartDate)
            assertThat(request.insuranceEndDate).isEqualTo(contract.insuranceEndDate)
            assertThat(request.coverageIds).contains(coverages[0].id)
        }
    }

    @Nested
    inner class `계약 수정 함수는` {

        @Test
        fun `담보, 계약기간, 계약상태, 보험료 정보를 수정한다`() {
            // given
            val saveRequest = ContractSaveRequest(
                productId = product.id,
                insuranceStartDate = LocalDate.now(),
                insuranceEndDate = LocalDate.now().plusMonths(1),
                coverageIds = listOf(coverage1.id)
            )
            val contractId = contractService.saveContract(saveRequest)

            val modifyRequest = ContractModifyRequest(
                contractId = contractId,
                addCoverageIds = listOf(coverage2.id),
                deleteCoverageIds = listOf(coverage1.id),
                insuranceEndDate = LocalDate.of(2024, 4, 1),
                contractStatus = ContractStatus.WITHDRAW.toString()
            )

            // when
            contractService.modifyContract(modifyRequest)

            // then
            val contract = contractRepository.findByIdOrNull(contractId)!!
            val contractCoverages = contractCoverageRepository.findByContractId(contract.id)
            val coverageIds = contractCoverages.map { it.coverageId }
            val calculateAmount = coverageService.calculateAmount(contract.productId, contract.calculatePeriod(), coverageIds)

            assertThat(modifyRequest.addCoverageIds).containsExactly(contractCoverages[0].coverageId)
            assertThat(modifyRequest.insuranceEndDate).isEqualTo(contract.insuranceEndDate)
            assertThat(modifyRequest.contractStatus).isEqualTo(contract.status.toString())
            assertThat(calculateAmount).isEqualTo(contract.totalAmount)
        }

        @Test
        fun `계약 상태가 만료 상태이면 예외를 던진다`() {
            // given
            val saveRequest = ContractSaveRequest(
                productId = product.id,
                insuranceStartDate = LocalDate.now(),
                insuranceEndDate = LocalDate.now().plusMonths(1),
                coverageIds = listOf(coverage1.id)
            )
            val contractId = contractService.saveContract(saveRequest)

            val modifyRequest = ContractModifyRequest(
                contractId = contractId,
                addCoverageIds = listOf(coverage2.id),
                deleteCoverageIds = listOf(coverage1.id),
                insuranceEndDate = LocalDate.of(2024, 4, 1),
                contractStatus = ContractStatus.WITHDRAW.toString()
            )

            // when & then
            val contract = contractRepository.findByIdOrNull(contractId)!!
            contract.updateStatus(ContractStatus.EXPIRED)

            assertThatThrownBy { contractService.modifyContract(modifyRequest) }
                .isExactlyInstanceOf(BusinessException::class.java)
                .hasMessage(ResponseCode.ALREADY_EXPIRED.message)
        }

        @Test
        fun `수정 계약 날짜가 금월보다 작거나 같으면 예외를 던진다`() {
            // given
            val saveRequest = ContractSaveRequest(
                productId = product.id,
                insuranceStartDate = LocalDate.now(),
                insuranceEndDate = LocalDate.now().plusMonths(1),
                coverageIds = listOf(coverage1.id)
            )
            val contractId = contractService.saveContract(saveRequest)

            val modifyRequest = ContractModifyRequest(
                contractId = contractId,
                addCoverageIds = listOf(coverage2.id),
                deleteCoverageIds = listOf(coverage1.id),
                insuranceEndDate = LocalDate.now().minusMonths(2),
                contractStatus = ContractStatus.WITHDRAW.toString()
            )

            // when & then
            assertThatThrownBy { contractService.modifyContract(modifyRequest) }
                .isExactlyInstanceOf(BusinessException::class.java)
                .hasMessage(ResponseCode.INVALID_INSURANCE_END_DATE.message)
        }
    }

    @Nested
    inner class `findExpiringContractsOneWeekBefore 함수는` {

        @Test
        fun `계약 만기가 일주일 남은 계약들을 조회한다`() {
            // given
            val today = LocalDate.now()
            val saveRequest = ContractSaveRequest(
                productId = product.id,
                insuranceStartDate = today.minusMonths(1).plusDays(7),
                insuranceEndDate = today.plusDays(7),
                coverageIds = listOf(coverage1.id)
            )
            contractService.saveContract(saveRequest)

            // when
            val contracts = contractService.findExpiringContractsOneWeekBefore()

            // then
            assertThat(contracts[0].insuranceEndDate).isEqualTo(today.plusDays(7))
        }
    }

}