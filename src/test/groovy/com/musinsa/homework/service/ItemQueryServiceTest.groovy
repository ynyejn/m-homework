package com.musinsa.homework.service

import com.musinsa.homework.entity.Brand
import com.musinsa.homework.entity.Category
import com.musinsa.homework.entity.Item
import com.musinsa.homework.repository.item.ItemRepository
import spock.lang.Specification

class ItemQueryServiceTest extends Specification {
    ItemRepository itemRepository = Mock(ItemRepository)
    ItemQueryService itemQueryService

    void setup() {
        itemQueryService = new ItemQueryService(itemRepository)
    }
    def "getLowestPricesByCategory 호출시 itemRepository의 getLowestPricesByCategory가 호출되고, 데이터가 있는 경우 반환된다."() {
        given:
        def givenBrand = new Brand("브랜드1")
        def givenBrand2 = new Brand("브랜드2")
        def givenCategory = new Category(1, "상의")
        def givenCategory2 = new Category(2, "바지")
        def givenItem = new Item(givenBrand, givenCategory, 10000)
        def givenItem2 = new Item(givenBrand2, givenCategory2, 20000)
        itemRepository.getLowestPricesByCategory() >> [givenItem, givenItem2]

        when:
        def response = itemQueryService.getLowestPricesByCategory()

        then:
        response.size() == 2
        response[0].category.name == "상의"
        response[0].price == 10000
        response[1].category.name == "바지"
        response[1].price == 20000
    }

    def "getLowestPriceBrand 호출 시 브랜드가 반환된다."() {
        given:
        def lowestPriceBrand = new Brand("브랜드1")
        itemRepository.getLowestPriceBrand() >> lowestPriceBrand

        when:
        def result = itemQueryService.getLowestPriceBrand()

        then:
        result.name == "브랜드1"
    }

    def "getItemsByBrand 호출 시 브랜드에 해당하는 아이템 리스트가 반환된다."() {
        given:
        def givenBrand = new Brand("브랜드1")
        def givenCategory = new Category("상의")
        def givenItem = new Item(givenBrand, givenCategory, 10000)

        when:
        def response = itemQueryService.getItemsByBrand(givenBrand)

        then:
        1 * itemRepository.findByBrand(givenBrand) >> {
            Brand brand ->
                assert brand == givenBrand
                [givenItem]
        }
        response.size() == 1
        response[0].brand.name == "브랜드1"
        response[0].price == 10000
    }
}
