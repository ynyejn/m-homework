package com.musinsa.homework.controller.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.musinsa.homework.entity.Brand
import com.musinsa.homework.repository.BrandRepository
import com.musinsa.homework.repository.item.ItemRepository
import com.musinsa.homework.service.AdminApplicationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AdminApiControllerItTest extends Specification {
    @Autowired
    MockMvc mockMvc
    @Autowired
    ObjectMapper objectMapper
    @Autowired
    AdminApplicationService adminApplicationService
    @Autowired
    BrandRepository brandRepository
    @Autowired
    ItemRepository itemRepository

    def "브랜드 생성, 상품 CRUD 작업을 수행할 수 있다."() {
        given:
        def brandName = "브랜드A"
        def itemPrice = 10000

        when: "브랜드 생성"
        def brandCreateResult = mockMvc.perform(
                MockMvcRequestBuilders.post("/v1/admin/brand-item-management")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "brandOperation": {
                                    "action": "CREATE",
                                    "brandName": "$brandName"
                                }
                            }
                        """)
        )
        then:
        brandCreateResult.andExpect(status().isOk())
                .andExpect(jsonPath('$.data.result').value(true))

        and:
        def createdBrand = brandRepository.findByName(brandName).get()
        createdBrand.name == brandName

        when: "상품 생성"
        def itemCreateResult = mockMvc.perform(
                MockMvcRequestBuilders.post("/v1/admin/brand-item-management")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "itemOperation": {
                                    "action": "CREATE",
                                    "brandId": $createdBrand.id,
                                    "categoryId": 1,
                                    "price": $itemPrice
                                }
                            }
                        """)
        )
        then:
        itemCreateResult.andExpect(status().isOk())
                .andExpect(jsonPath('$.data.result').value(true))

        and:
        def createdItem = itemRepository.findByBrand(createdBrand).get(0)
        createdItem.brand.id == createdBrand.id
        createdItem.price == itemPrice

        when: "상품 수정"
        def itemUpdateResult = mockMvc.perform(
                MockMvcRequestBuilders.post("/v1/admin/brand-item-management")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "itemOperation": {
                                    "action": "UPDATE",
                                    "itemId": $createdItem.id,
                                    "brandId": $createdBrand.id,
                                    "categoryId": 1,
                                    "price": 15000
                                }
                            }
                        """)
        )
        then:
        itemUpdateResult.andExpect(status().isOk())
                .andExpect(jsonPath('$.data.result').value(true))

        and:
        def updatedItem = itemRepository.findById(createdItem.id).get()
        updatedItem.price == 15000

        when: "상품 삭제"
        def itemDeleteResult = mockMvc.perform(
                MockMvcRequestBuilders.post("/v1/admin/brand-item-management")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "itemOperation": {
                                    "action": "DELETE",
                                    "itemId": $createdItem.id
                                }
                            }
                        """)
        )
        then:
        itemDeleteResult.andExpect(status().isOk())
                .andExpect(jsonPath('$.data.result').value(true))

        and:
        def deletedItem = itemRepository.findById(createdItem.id)
        deletedItem.isEmpty()
    }


    def "브랜드 생성 시 이미 존재하는 브랜드명이면 에러를 반환한다."() {
        given:
        def brandName = "브랜드A"
        brandRepository.save(new Brand(brandName))

        when:
        def result = mockMvc.perform(
                MockMvcRequestBuilders.post("/v1/admin/brand-item-management")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "brandOperation": {
                                    "action": "CREATE",
                                    "brandName": "$brandName"
                                }
                            }
                        """)
        )

        then:
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath('$.success').value(false))
                .andExpect(jsonPath('$.message').value("이미 존재하는 브랜드 명입니다."))
    }

    def "BrandOperation과 ItemOperation이 동시에 입력되면 에러가 발생한다"() {
        when:
        def result = mockMvc.perform(
                MockMvcRequestBuilders.post("/v1/admin/brand-item-management")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "brandOperation": {
                                    "action": "CREATE",
                                    "brandName": "브랜드A"
                                },
                                "itemOperation": {
                                    "action": "CREATE",
                                    "brandId": 1,
                                    "categoryId": 1,
                                    "price": 10000
                                }
                            }
                        """)
        )

        then:
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath('$.success').value(false))
                .andExpect(jsonPath('$.message').value("BrandOperation 또는 ItemOperation 중 하나만 입력되어야 합니다."))
    }
    def "상품 생성 시 가격이 음수이면 에러가 발생한다"() {
        when:
        def result = mockMvc.perform(
                MockMvcRequestBuilders.post("/v1/admin/brand-item-management")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "itemOperation": {
                                    "action": "CREATE",
                                    "brandId": 1,
                                    "categoryId": 1,
                                    "price": -1000
                                }
                            }
                        """)
        )

        then:
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath('$.success').value(false))
                .andExpect(jsonPath('$.message').value("상품 가격은 0 이상이어야 합니다."))
    }

    def "필수 필드가 누락되면 에러가 발생한다"() {
        when:
        def result = mockMvc.perform(
                MockMvcRequestBuilders.post("/v1/admin/brand-item-management")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "brandOperation": {
                                    "action": "CREATE"
                                }
                            }
                        """)
        )

        then:
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath('$.success').value(false))
                .andExpect(jsonPath('$.message').value("브랜드 이름은 필수입니다."))
    }
}
