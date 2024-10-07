package com.musinsa.homework.service

import com.musinsa.homework.controller.api.request.ActionType
import com.musinsa.homework.controller.api.request.BrandItemManagementRequestDto
import com.musinsa.homework.controller.api.response.ResultResponseDto
import spock.lang.Specification


class AdminApplicationServiceTest extends Specification {
    BrandCommandService brandCommandService = Mock(BrandCommandService)
    ItemCommandService itemCommandService = Mock(ItemCommandService)
    AdminApplicationService adminApplicationService

    void setup() {
        adminApplicationService = new AdminApplicationService(brandCommandService, itemCommandService)
    }

    def "manageBrandAndItems 호출시 브랜드 관련 작업을 처리한다."() {
        given:
        def brandOperation = new BrandItemManagementRequestDto.BrandOperation(ActionType.CREATE, "브랜드A")
        def request = new BrandItemManagementRequestDto()
        request.brandOperation = brandOperation
        def expectedResult = ResultResponseDto.success()

        when:
        def response = adminApplicationService.manageBrandAndItems(request)

        then:
        1 * brandCommandService.processBrandOperation(brandOperation) >> expectedResult
        0 * itemCommandService.processItemOperation(_)
        response == expectedResult
    }

    def "manageBrandAndItems 호출시 아이템 관련 작업을 처리한다."() {
        given:
        def itemOperation = new BrandItemManagementRequestDto.ItemOperation()
        itemOperation.action = ActionType.CREATE
        itemOperation.brandId = 1
        itemOperation.categoryId = 1
        itemOperation.price = 10000

        def request = new BrandItemManagementRequestDto()
        request.itemOperation= itemOperation
        def expectedResult = ResultResponseDto.success()

        when:
        def response = adminApplicationService.manageBrandAndItems(request)

        then:
        1 * itemCommandService.processItemOperation(itemOperation) >> expectedResult
        0 * brandCommandService.processBrandOperation(_)
        response == expectedResult
    }

    def "manageBrandAndItems 호출시 request가 null이면 아무 작업도 하지 않고 실패를 반환한다."() {
        given:
        def request = new BrandItemManagementRequestDto()

        when:
        def response = adminApplicationService.manageBrandAndItems(request)

        then:
        0 * brandCommandService.processBrandOperation(_)
        0 * itemCommandService.processItemOperation(_)
        response.result == false
        response.message == "요청이 잘못되었습니다."
    }

}
