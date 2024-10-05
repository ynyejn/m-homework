package com.musinsa.homework.controller

import com.musinsa.homework.common.config.GlobalExceptionHandler
import com.musinsa.homework.controller.response.LowestPriceByCategoryResponseDto
import com.musinsa.homework.service.ItemApplicationService
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

class ItemApiControllerTest extends Specification {
    ItemApplicationService itemApplicationService = Mock(ItemApplicationService)

    ItemApiController itemApiController
    MockMvc mockMvc

    void setup() {
        itemApiController = new ItemApiController(itemApplicationService)
        mockMvc = MockMvcBuilders.standaloneSetup(itemApiController).build()
        def exceptionResolver = new ExceptionHandlerExceptionResolver()
        exceptionResolver.afterPropertiesSet()
        mockMvc = MockMvcBuilders.standaloneSetup(itemApiController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setHandlerExceptionResolvers(exceptionResolver)
                .build()
    }

    def "getLowestPricesByCategory 호출 시 성공한다."() {
        given:
        def expectedResponse = new LowestPriceByCategoryResponseDto(
                [
                        [category: "상의", brand: "C", price: "10,000"],
                        [category: "아우터", brand: "E", price: "5,000"],
                        [category: "바지", brand: "D", price: "3,000"],
                        [category: "스니커즈", brand: "G", price: "9,000"],
                        [category: "가방", brand: "A", price: "2,000"],
                        [category: "모자", brand: "D", price: "1,500"],
                        [category: "양말", brand: "I", price: "1,700"],
                        [category: "액세서리", brand: "F", price: "1,900"]
                ],
                "34,100"
        )
        itemApplicationService.getLowestPricesByCategory() >> expectedResponse

        when:
        def result = mockMvc.perform(get("/v1/items/lowest-prices")
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath('$.data.items').isArray())
                .andExpect(jsonPath('$.data.items.length()').value(8))
                .andExpect(jsonPath('$.data.items[0].category').value("상의"))
                .andExpect(jsonPath('$.data.items[0].brand').value("C"))
                .andExpect(jsonPath('$.data.items[0].price').value("10,000"))
                .andExpect(jsonPath('$.data.totalPrice').value("34,100"))
    }


}
