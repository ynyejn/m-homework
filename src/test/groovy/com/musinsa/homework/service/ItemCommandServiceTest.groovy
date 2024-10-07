package com.musinsa.homework.service

import com.musinsa.homework.controller.api.request.BrandItemManagementRequestDto
import com.musinsa.homework.entity.Brand
import com.musinsa.homework.entity.Category
import com.musinsa.homework.entity.Item
import com.musinsa.homework.repository.BrandRepository
import com.musinsa.homework.repository.CategoryRepository
import com.musinsa.homework.repository.item.ItemRepository
import com.musinsa.homework.common.exception.ApiException
import com.musinsa.homework.common.exception.ApiErrorCode
import spock.lang.Specification

class ItemCommandServiceTest extends Specification {
    ItemRepository itemRepository = Mock(ItemRepository)
    BrandRepository brandRepository = Mock(BrandRepository)
    CategoryRepository categoryRepository = Mock(CategoryRepository)

    ItemCommandService itemCommandService

    void setup() {
        itemCommandService = new ItemCommandService(itemRepository, brandRepository, categoryRepository)
    }

    def "processItemOperation 호출시 브랜드 생성에 성공한다."() {
        given:
        def operation = new BrandItemManagementRequestDto.ItemOperation()
        operation.action = "CREATE"
        operation.brandId = 1L
        operation.categoryId = 1L
        operation.price = 1000
        def brand = new Brand("브랜드A")
        def category = new Category("상의")

        brandRepository.findById(1L) >> Optional.of(brand)
        categoryRepository.findById(1L) >> Optional.of(category)

        when:
        def response = itemCommandService.processItemOperation(operation)

        then:
        1 * itemRepository.save(_ as Item)
        response.result == true
    }

    def "processItemOperation 호출시 브랜드 수정에 성공한다."() {
        given:
        def operation = new BrandItemManagementRequestDto.ItemOperation()
        operation.action = "UPDATE"
        operation.brandId = 1L
        operation.categoryId = 1L
        operation.itemId= 1L
        operation.price = 2000
        def brand = new Brand("브랜드A")
        def category = new Category("상의")
        def item = new Item(brand, category, 1000)

        brandRepository.findById(1L) >> Optional.of(brand)
        categoryRepository.findById(1L) >> Optional.of(category)
        itemRepository.findById(1L) >> Optional.of(item)

        when:
        def response = itemCommandService.processItemOperation(operation)

        then:
        response.result == true
        item.price == 2000
    }

    def "processItemOperation 호출시 브랜드 삭제에 성공한다."() {
        given:
        def operation = new BrandItemManagementRequestDto.ItemOperation()
        operation.action = "DELETE"
        operation.itemId = 1L

        def item = new Item(new Brand("브랜드A"), new Category("상의"), 1000)

        itemRepository.findById(1L) >> Optional.of(item)

        when:
        def response = itemCommandService.processItemOperation(operation)

        then:
        1 * itemRepository.delete(item)
        response.result == true
    }


    def "createItem 호출시 브랜드가 없으면 ApiException을 반환한다."() {
        given:
        brandRepository.findById(1L) >> Optional.empty()

        when:
        itemCommandService.createItem(1L, 1L, 1000)

        then:
        def e = thrown(ApiException)
        e.getApiErrorCode() == ApiErrorCode.BRAND_NOT_FOUND
    }

    def "createItem 호출시 카테고리가 없으면 ApiException을 반환한다."() {
        given:
        brandRepository.findById(1L) >> Optional.of(new Brand("브랜드A"))
        categoryRepository.findById(1L) >> Optional.empty()

        when:
        itemCommandService.createItem(1L, 1L, 1000)

        then:
        def e = thrown(ApiException)
        e.getApiErrorCode() == ApiErrorCode.CATEGORY_NOT_FOUND
    }

    def "updateItem 호출시 아이템이 없으면 ApiException을 반환한다."() {
        given:
        brandRepository.findById(1L) >> Optional.of(new Brand("브랜드A"))
        categoryRepository.findById(1L) >> Optional.of(new Category("상의"))
        itemRepository.findById(1L) >> Optional.empty()

        when:
        itemCommandService.updateItem(1L, 1L, 1L, 2000)

        then:
        def e = thrown(ApiException)
        e.getApiErrorCode() == ApiErrorCode.ITEM_NOT_FOUND
    }
    def "deleteItem 호출시 아이템이 없으면 ApiException을 반환한다."() {
        given:
        itemRepository.findById(1L) >> Optional.empty()

        when:
        itemCommandService.deleteItem(1L)

        then:
        def e = thrown(ApiException)
        e.getApiErrorCode() == ApiErrorCode.ITEM_NOT_FOUND
    }
}
