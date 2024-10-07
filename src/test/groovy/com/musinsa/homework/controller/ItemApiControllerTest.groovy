package com.musinsa.homework.controller

import com.musinsa.homework.common.config.GlobalExceptionHandler
import com.musinsa.homework.controller.response.CategoryPriceRangeResponseDto
import com.musinsa.homework.controller.response.LowestPriceBrandResponseDto
import com.musinsa.homework.controller.response.LowestPriceByCategoryResponseDto
import com.musinsa.homework.service.ItemQueryService
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

class ItemApiControllerTest extends Specification {
    ItemQueryService itemQueryService = Mock(ItemQueryService)

    ItemApiController itemApiController
    MockMvc mockMvc

    void setup() {
        itemApiController = new ItemApiController(itemQueryService)
        mockMvc = MockMvcBuilders.standaloneSetup(itemApiController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setHandlerExceptionResolvers(new ExceptionHandlerExceptionResolver())
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
        itemQueryService.getLowestPricesByCategory() >> expectedResponse

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

    def "getLowestPriceBrand 호출 시 성공한다."() {
        given:
        def categoryPrices = [LowestPriceBrandResponseDto.CategoryPrice.builder().카테고리("상의").가격("10,000").build()]
        def lowestPriceInfo = LowestPriceBrandResponseDto.LowestPriceInfo.builder().브랜드("브랜드A").카테고리(categoryPrices).총액("10,000").build()
        def expectedResponse = LowestPriceBrandResponseDto.builder().최저가(lowestPriceInfo).build()

        itemQueryService.getLowestPriceBrand() >> expectedResponse

        when:
        def result = mockMvc.perform(get("/v1/items/lowest-price-brand")
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath('$.data.최저가.브랜드').value("브랜드A"))
                .andExpect(jsonPath('$.data.최저가.카테고리[0].카테고리').value("상의"))
                .andExpect(jsonPath('$.data.최저가.카테고리[0].가격').value("10,000"))
                .andExpect(jsonPath('$.data.최저가.총액').value("10,000"))
    }

    def "getLowestPriceBrand 호출 시 데이터가 없으면 empty response를 반환한다."() {
        given:
        itemQueryService.getLowestPriceBrand() >> LowestPriceBrandResponseDto.empty()

        when:
        def result = mockMvc.perform(get("/v1/items/lowest-price-brand")
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath('$.data.최저가.브랜드').value(""))
                .andExpect(jsonPath('$.data.최저가.카테고리').isEmpty())
                .andExpect(jsonPath('$.data.최저가.총액').value("0"))
    }

    def "getCategoryPriceRange 호출 시 성공한다."() {
        given:
        def categoryName = "상의"
        def expectedResponse = CategoryPriceRangeResponseDto.builder()
                .카테고리(categoryName)
                .최저가([
                        CategoryPriceRangeResponseDto.PriceBrandInfo.builder()
                                .브랜드("C")
                                .가격("10,000")
                                .build()
                ])
                .최고가([
                        CategoryPriceRangeResponseDto.PriceBrandInfo.builder()
                                .브랜드("D")
                                .가격("20,000")
                                .build()
                ])
                .build()
        itemQueryService.getCategoryPriceRange(categoryName) >> expectedResponse


        when:
        def result = mockMvc.perform(get("/v1/items/categories/{categoryName}/prices", categoryName)
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath('$.data.카테고리').value("상의"))
                .andExpect(jsonPath('$.data.최저가').isArray())
                .andExpect(jsonPath('$.data.최저가.length()').value(1))
                .andExpect(jsonPath('$.data.최저가[0].브랜드').value("C"))
                .andExpect(jsonPath('$.data.최저가[0].가격').value("10,000"))
                .andExpect(jsonPath('$.data.최고가').isArray())
                .andExpect(jsonPath('$.data.최고가.length()').value(1))
                .andExpect(jsonPath('$.data.최고가[0].브랜드').value("D"))
                .andExpect(jsonPath('$.data.최고가[0].가격').value("20,000"))
    }


}
