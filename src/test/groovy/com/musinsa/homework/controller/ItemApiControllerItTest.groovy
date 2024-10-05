package com.musinsa.homework.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.musinsa.homework.service.ItemApplicationService
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
    ItemApplicationService itemApplicationService

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
                .andExpect(jsonPath('$.data.totalPrice').value("34,100"))

        and: "응답 내용 출력"
        def data = result.andReturn().response.contentAsString
        println "result data: $data"
    }
}
