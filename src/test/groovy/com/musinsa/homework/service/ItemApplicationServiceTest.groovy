package com.musinsa.homework.service

import com.musinsa.homework.controller.response.LowestPriceBrandResponseDto
import com.musinsa.homework.entity.Brand
import com.musinsa.homework.entity.Category
import com.musinsa.homework.entity.Item
import spock.lang.Specification

class ItemApplicationServiceTest extends Specification {
    ItemApplicationService itemApplicationService

    ItemQueryService itemQueryService = Mock(ItemQueryService)
    ItemCommandService itemCommandService = Mock(ItemCommandService)

    void setup() {
        itemApplicationService = new ItemApplicationService(itemQueryService, itemCommandService)
    }

    def "getLowestPricesByCategory 호출시 itemQueryService의 getLowestPricesByCategory가 호출된다."() {
       when:
        itemApplicationService.getLowestPricesByCategory()

        then:
        1 * itemQueryService.getLowestPricesByCategory()
    }
    def "getLowestPriceBrand 호출시 브랜드가 없으면 빈 응답을 반환한다."() {
        given:
        itemQueryService.getLowestPriceBrand() >> null

        when:
        def response = itemApplicationService.getLowestPriceBrand()

        then:
        response.최저가.브랜드 == ""
        response.최저가.총액 == "0"
        response.최저가.카테고리.size() == 0

    }
    def "getLowestPriceBrand 호출시 브랜드가 없으면 itemQueryService의 getLowestPriceBrand만 호출된다."() {

        when:
        itemApplicationService.getLowestPriceBrand()

        then:
        1 * itemQueryService.getLowestPriceBrand() >> null
        0 * itemQueryService.getItemsByBrand(_)
    }

        def "getLowestPriceBrand 호출시 브랜드가 있으면 해당 브랜드의 아이템 리스트가 반환된다."() {
        given:
        def givenBrand = new Brand("브랜드1")
        def givenCategory = new Category("상의")
        def givenItem = new Item(givenBrand, givenCategory, 10000)
        def items = [givenItem]

        itemQueryService.getLowestPriceBrand() >> givenBrand
        itemQueryService.getItemsByBrand(givenBrand) >> items

        when:
        def response = itemApplicationService.getLowestPriceBrand()

        then:
        response.최저가.브랜드 == "브랜드1"
        response.최저가.총액 == "10,000"
        response.최저가.카테고리.size() == 1
        response.최저가.카테고리[0].카테고리 == "상의"
        response.최저가.카테고리[0].가격 == "10,000"
    }

}
