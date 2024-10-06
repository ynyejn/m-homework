package com.musinsa.homework.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.musinsa.homework.service.ItemQueryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import spock.lang.Specification

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
class ItemApiControllerItTest extends Specification {
    @Autowired
    MockMvc mockMvc
    @Autowired
    ObjectMapper objectMapper
    @Autowired
    ItemQueryService itemQueryService

    def "getLowestPricesByCategory 호출시 카테고리별 최저가 상품 정보를 반환한다."() {
        when:
        def result = mockMvc.perform(MockMvcRequestBuilders.get("/v1/items/lowest-prices"))

        then:
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath('$.success').value(true))
                .andExpect(jsonPath('$.data.items').isArray())
                .andExpect(jsonPath('$.data.items.length()').value(8))
                .andExpect(jsonPath('$.data.items[0].category').value("상의"))
                .andExpect(jsonPath('$.data.items[0].brand').value("C"))
                .andExpect(jsonPath('$.data.items[0].price').value("10,000"))
                .andExpect(jsonPath('$.data.items[1].category').value("아우터"))
                .andExpect(jsonPath('$.data.items[1].brand').value("E"))
                .andExpect(jsonPath('$.data.items[1].price').value("5,000"))
                .andExpect(jsonPath('$.data.items[3].category').value("스니커즈"))
                .andExpect(jsonPath('$.data.items[3].brand').value("G"))
                .andExpect(jsonPath('$.data.items[3].price').value("9,000"))
                .andExpect(jsonPath('$.data.totalPrice').value("34,100"))

        and: "응답 내용 출력"
        def data = result.andReturn().response.contentAsString
        println "result data: $data"
    }

    def "getLowestPriceBrand 호출시 브랜드별 최저가 상품 정보를 반환한다."() {
        when:
        def result = mockMvc.perform(MockMvcRequestBuilders.get("/v1/items/lowest-price-brand"))

        then:
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath('$.success').value(true))
                .andExpect(jsonPath('$.data.최저가').exists())
                .andExpect(jsonPath('$.data.최저가.브랜드').value("D"))
                .andExpect(jsonPath('$.data.최저가.카테고리').isArray())
                .andExpect(jsonPath('$.data.최저가.카테고리.length()').value(8))
                .andExpect(jsonPath('$.data.최저가.총액').value("36,100"))
    }

    def "getCategoryPriceRange 호출 시 카테고리별 최저/최고 가격 정보를 반환한다."() {
        given:
        def categoryName = "상의"

        when:
        def result = mockMvc.perform(MockMvcRequestBuilders.get("/v1/items/categories/{categoryName}/prices", categoryName)
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath('$.success').value(true))
                .andExpect(jsonPath('$.data.카테고리').value("상의"))
                .andExpect(jsonPath('$.data.최저가').isArray())
                .andExpect(jsonPath('$.data.최저가.length()').value(1))
                .andExpect(jsonPath('$.data.최저가[0].브랜드').value("C"))
                .andExpect(jsonPath('$.data.최저가[0].가격').value("10,000"))
                .andExpect(jsonPath('$.data.최고가').isArray())
                .andExpect(jsonPath('$.data.최고가.length()').value(1))
                .andExpect(jsonPath('$.data.최고가[0].브랜드').value("I"))
                .andExpect(jsonPath('$.data.최고가[0].가격').value("11,400"))

        and: "응답 내용 출력"
        def data = result.andReturn().response.contentAsString
        println "result data: $data"
    }

    def "getCategoryPriceRange 호출 시 카테고리명을 잘못 입력하면 404 에러를 반환한다."() {
        given:
        def categoryName = "없는카테고리"

        when:
        def result = mockMvc.perform(MockMvcRequestBuilders.get("/v1/items/categories/{categoryName}/prices", categoryName)
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath('$.success').value(false))
                .andExpect(jsonPath('$.message').value("해당 카테고리를 찾을 수 없습니다."))
    }

}
