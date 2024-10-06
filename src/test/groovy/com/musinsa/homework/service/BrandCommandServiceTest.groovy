package com.musinsa.homework.service

import com.musinsa.homework.common.exception.ApiException
import com.musinsa.homework.controller.request.ActionType
import com.musinsa.homework.controller.request.BrandItemManagementRequestDto
import com.musinsa.homework.entity.Brand
import com.musinsa.homework.repository.BrandRepository
import spock.lang.Specification
import spock.lang.Unroll

class BrandCommandServiceTest extends Specification {
    BrandRepository brandRepository = Mock(BrandRepository)
    BrandCommandService brandCommandService

    void setup() {
        brandCommandService = new BrandCommandService(brandRepository)
    }


    def "processBrandOperation 호출시 브랜드 저장된다."() {
        given:
        def givenBrandName = "브랜드A"
        def givenBrandOperation = new BrandItemManagementRequestDto.BrandOperation()
        givenBrandOperation.action = ActionType.CREATE
        givenBrandOperation.brandName = givenBrandName

        when:
        brandCommandService.processBrandOperation(givenBrandOperation)

        then:
        1 * brandRepository.save(*_) >> {
            Brand brand ->
                assert brand.name == givenBrandName
        }
    }

    @Unroll
    def "processBrandOperation 호출시 action이 CREATE가 아니면 에러 응답한다."() {
        given:
        def givenBrandOperation = new BrandItemManagementRequestDto.BrandOperation()
        givenBrandOperation.action = action

        when:
        brandCommandService.processBrandOperation(givenBrandOperation)

        then:
        def e= thrown(ApiException)
        e.message == "허용되지 않은 액션입니다."

        where:
        action << [ActionType.DELETE, ActionType.UPDATE]
    }

    def "createBrand 호출시 인자가 그대로 넘어가고 저장된다."() {
        given:
        def givenBrandName = "브랜드A"

        when:
        brandCommandService.createBrand(givenBrandName)

        then:
        1 * brandRepository.save(*_) >> {
            Brand brand ->
                assert brand.name == givenBrandName
        }
    }

    def "createBrand 호출시 이미 존재하는 브랜드명이면 실패 응답한다"() {
        given:
        def givenBrandName = "브랜드A"
        brandRepository.existsByName(givenBrandName) >> true

        when:
        def response =  brandCommandService.createBrand(givenBrandName)

        then:
        response.result == false
        response.message == "이미 존재하는 브랜드입니다."
    }

}
