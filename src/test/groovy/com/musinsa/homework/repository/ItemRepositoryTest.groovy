package com.musinsa.homework.repository

import com.musinsa.homework.common.config.TestQuerydslConfig
import com.musinsa.homework.entity.Brand
import com.musinsa.homework.entity.Category
import com.musinsa.homework.entity.Item
import com.musinsa.homework.repository.item.ItemRepository
import com.musinsa.homework.repository.item.ItemRepositoryInterfaceImpl
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@ActiveProfiles("test")
@DataJpaTest
@Import(TestQuerydslConfig)
class ItemRepositoryTest extends Specification {
    @Autowired
    EntityManager entityManager
    @Autowired
    JPAQueryFactory jpaQueryFactory
    @Autowired
    BrandRepository brandRepository
    @Autowired
    ItemRepository itemRepository

    ItemRepositoryInterfaceImpl itemRepositoryInterface

    Brand givenBrand

    def setup() {
        itemRepositoryInterface = new ItemRepositoryInterfaceImpl(jpaQueryFactory)
        prepareTestData()
    }

    def "getLowestPricesByCategory 호출 시 카테고리별 최저상품이 조회된다."() {
        when:
        def result = itemRepositoryInterface.getLowestPricesByCategory()

        then:
        result.size() == 3
        result.every { it instanceof Item }
        result.collect { it.category.name }.toSet() == ["상의", "바지", "스니커즈"].toSet()
        result.find { it.category.name == "상의" }.price == 10000
        result.find { it.category.name == "바지" }.price == 20000
        result.find { it.category.name == "스니커즈" }.price == 30000
    }

    def "getLowestPriceBrand 호출 시 총 가격이 가장 낮은 브랜드가 조회된다."() {
        when:
        def result = itemRepositoryInterface.getLowestPriceBrand()

        then:
        result != null
        result.name == "브랜드A"  // 브랜드A 의 가격이 가장 낮음
    }

    def "getLowestPriceBrand 호출 시 데이터가 없으면 null 반환한다."() {
        given:
        itemRepository.deleteAll()

        when:
        def result = itemRepositoryInterface.getLowestPriceBrand()

        then:
        result == null
    }

    def "findByBrand 호출 시 브랜드의 모든 상품을 반환하고 카테고리와 브랜드가 즉시 로딩된다"() {
        when:
        def result = itemRepository.findByBrand(givenBrand)

        then:
        result.size() == 3
        result.every { item ->
            item.brand.name == "브랜드A"
            item.category != null
            // 엔티티 초기화 확인
            org.hibernate.Hibernate.isInitialized(item.category)
            org.hibernate.Hibernate.isInitialized(item.brand)
        }
        result.collect { it.price }.sort() == [10000, 20000, 30000]
    }

    def "findItemsWithExtremePrice 호출 시 해당 카테고리의 최저가 상품 목록이 반환된다."() {
        given:
        def categoryName = "상의"

        when:
        def result = itemRepositoryInterface.findItemsWithExtremePrice(categoryName, true)

        then:
        result.size() == 1
        result[0].price == 10000
    }

    def "findItemsWithExtremePrice 호출 시 해당 카테고리의 최고가 상품 목록이 반환된다."() {
        given:
        def categoryName = "상의"

        when:
        def result = itemRepositoryInterface.findItemsWithExtremePrice(categoryName, false)

        then:
        result.size() == 1
        result[0].price == 15000
    }
    def "findItemsWithExtremePrice 호출 시 해당 카테고리 이름이 없으면 빈 목록이 반환된다."() {
        given:
        def categoryName = "없는카테고리"

        when:
        def result = itemRepositoryInterface.findItemsWithExtremePrice(categoryName, true)

        then:
        result.size() == 0
    }




    private void prepareTestData() {
        def givenCategory = new Category("상의")
        def givenCategory2 = new Category("바지")
        def givenCategory3 = new Category("스니커즈")

        givenBrand = new Brand("브랜드A")
        def givenBrand2 = new Brand("브랜드B")

        entityManager.persist(givenCategory)
        entityManager.persist(givenCategory2)
        entityManager.persist(givenCategory3)
        entityManager.persist(givenBrand)
        entityManager.persist(givenBrand2)

        entityManager.persist(new Item(givenBrand, givenCategory, 10000))
        entityManager.persist(new Item(givenBrand2, givenCategory, 15000))
        entityManager.persist(new Item(givenBrand, givenCategory2, 20000))
        entityManager.persist(new Item(givenBrand2, givenCategory2, 25000))
        entityManager.persist(new Item(givenBrand, givenCategory3, 30000))
        entityManager.persist(new Item(givenBrand2, givenCategory3, 35000))

        entityManager.flush()
        entityManager.clear()
    }
}
