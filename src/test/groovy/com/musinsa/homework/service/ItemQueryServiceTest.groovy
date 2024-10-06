package com.musinsa.homework.service

import com.musinsa.homework.common.exception.ApiErrorCode
import com.musinsa.homework.common.exception.ApiException
import com.musinsa.homework.controller.response.CategoryPriceRangeResponseDto
import com.musinsa.homework.controller.response.LowestPriceBrandResponseDto
import com.musinsa.homework.controller.response.LowestPriceByCategoryResponseDto
import com.musinsa.homework.entity.Brand
import com.musinsa.homework.entity.Category
import com.musinsa.homework.entity.Item
import com.musinsa.homework.repository.CategoryRepository
import com.musinsa.homework.repository.item.ItemRepository
import spock.lang.Specification

class ItemQueryServiceTest extends Specification {
    ItemRepository itemRepository = Mock(ItemRepository)
    CategoryRepository categoryRepository = Mock(CategoryRepository)
    ItemQueryService itemQueryService

    void setup() {
        itemQueryService = new ItemQueryService(itemRepository,categoryRepository)
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
        response instanceof LowestPriceByCategoryResponseDto
        response.items.size() == 2
        response.items[0].category == "상의"
        response.items[0].price == "10,000"
        response.items[1].category == "바지"
        response.items[1].price == "20,000"
    }
    def "getLowestPricesByCategory 호출시 itemRepository의 getLowestPricesByCategory가 호출되고, 데이터가 없는 경우 빈 응답이 반환된다."() {
        given:
        itemRepository.getLowestPricesByCategory() >> []

        when:
        def response = itemQueryService.getLowestPricesByCategory()

        then:
        response instanceof LowestPriceByCategoryResponseDto
        response.items.size() == 0
    }

    def "getLowestPriceBrand 호출시 브랜드가 있으면 해당 브랜드의 아이템 리스트가 반환된다."() {
        given:
        def givenBrand = new Brand("브랜드1")
        def givenCategory = new Category("상의")
        def givenItem = new Item(givenBrand, givenCategory, 10000)
        def items = [givenItem]

        itemRepository.getLowestPriceBrand() >> givenBrand
        itemRepository.findByBrand(givenBrand) >> items

        when:
        def response = itemQueryService.getLowestPriceBrand()

        then:
        response instanceof LowestPriceBrandResponseDto
        response.최저가.브랜드 == "브랜드1"
        response.최저가.총액 == "10,000"
        response.최저가.카테고리.size() == 1
        response.최저가.카테고리[0].카테고리 == "상의"
        response.최저가.카테고리[0].가격 == "10,000"
    }

    def "getLowestPriceBrand 호출시 브랜드가 없으면 빈 응답이 반환된다."() {
        given:
        itemRepository.getLowestPriceBrand() >> null

        when:
        def response = itemQueryService.getLowestPriceBrand()

        then:
        response instanceof LowestPriceBrandResponseDto
        response.최저가.카테고리.size() == 0
        response.최저가.브랜드 == ""
        response.최저가.총액 == "0"
    }

    def "getCategoryPriceRange 호출 시 인자가 정상적으로 전달된다."() {
        given:
        def givenCategoryName = "상의"
        def givenCategory = new Category(1L, givenCategoryName)
        def itemsWithMinPrice = [
                new Item(new Brand("C"), givenCategory, 10000),
                new Item(new Brand("D"), givenCategory, 15000)
        ]
        def itemsWithMaxPrice = [
                new Item(new Brand("B"), givenCategory, 20000),
                new Item(new Brand("D"), givenCategory, 25000)
        ]

        when:
        itemQueryService.getCategoryPriceRange(givenCategoryName)

        then:
        1 * categoryRepository.findByName(givenCategoryName) >> Optional.of(givenCategory)
        1 * itemRepository.findItemsWithExtremePrice(givenCategory.getName(), true) >> itemsWithMinPrice
        1 * itemRepository.findItemsWithExtremePrice(givenCategory.getName(), false) >> itemsWithMaxPrice
    }

    def "getCategoryPriceRange 호출 시 성공한다."() {
        given:
        def categoryName = "상의"
        def category = new Category(1, categoryName)
        def brandA = new Brand("브랜드A")
        def brandB = new Brand("브랜드B")
        def minItem = new Item(brandA, category, 10000)
        def maxItem = new Item(brandB, category, 20000)

        categoryRepository.findByName(categoryName) >> Optional.of(category)
        itemRepository.findItemsWithExtremePrice(categoryName, true) >> [minItem]
        itemRepository.findItemsWithExtremePrice(categoryName, false) >> [maxItem]

        when:
        def response = itemQueryService.getCategoryPriceRange(categoryName)

        then:
        response instanceof CategoryPriceRangeResponseDto
        response.카테고리 == categoryName
        response.최저가[0].브랜드 == "브랜드A"
        response.최저가[0].가격 == "10,000"
        response.최고가[0].브랜드 == "브랜드B"
        response.최고가[0].가격 == "20,000"
    }

    def "getCategoryPriceRange 호출 시 카테고리가 존재하지 않으면 ApiException 발생한다."() {
        given:
        def categoryName = "없는카테고리"
        categoryRepository.findByName(categoryName) >> Optional.empty()

        when:
        itemQueryService.getCategoryPriceRange(categoryName)

        then:
        def exception = thrown(ApiException)
        exception.getApiErrorCode()== ApiErrorCode.CATEGORY_NOT_FOUND
    }

}
