package com.musinsa.homework.controller.api

import com.musinsa.homework.common.exception.GlobalExceptionHandler
import com.musinsa.homework.controller.api.request.BrandItemManagementRequestDto
import com.musinsa.homework.controller.api.response.ResultResponseDto
import com.musinsa.homework.service.AdminApplicationService
import org.springframework.http.MediaType

import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver
import spock.lang.Specification


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*


class AdminApiControllerTest extends Specification {
    AdminApplicationService adminApplicationService = Mock(AdminApplicationService)

    AdminApiController adminApiController
    MockMvc mockMvc

    void setup() {
        adminApiController = new AdminApiController(adminApplicationService)
        mockMvc = MockMvcBuilders.standaloneSetup(adminApiController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setHandlerExceptionResolvers(new ExceptionHandlerExceptionResolver())
                .build()
    }

    def "브랜드 생성, 상품 CRUD 작업을 수행할 수 있다."() {
        given:
        def brandName = "브랜드A"
        def itemPrice = 10000

        when: "브랜드 생성"
        def brandCreateResult = mockMvc.perform(
                post("/v1/admin/brand-item-management")
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
        1 * adminApplicationService.manageBrandAndItems(_ as BrandItemManagementRequestDto) >> ResultResponseDto.success()
        brandCreateResult.andExpect(status().isOk())
                .andExpect(jsonPath('$.data.result').value(true))

        when: "상품 생성"
        def itemCreateResult = mockMvc.perform(
                post("/v1/admin/brand-item-management")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "itemOperation": {
                                    "action": "CREATE",
                                    "brandId": 1,
                                    "categoryId": 1,
                                    "price": $itemPrice
                                }
                            }
                        """)
        )
        then:
        1 * adminApplicationService.manageBrandAndItems(_ as BrandItemManagementRequestDto) >> ResultResponseDto.success()
        itemCreateResult.andExpect(status().isOk())
                .andExpect(jsonPath('$.data.result').value(true))

        when: "상품 수정"
        def itemUpdateResult = mockMvc.perform(
                post("/v1/admin/brand-item-management")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "itemOperation": {
                                    "action": "UPDATE",
                                    "itemId": 1,
                                    "brandId": 1,
                                    "categoryId": 1,
                                    "price": 15000
                                }
                            }
                        """)
        )
        then:
        1 * adminApplicationService.manageBrandAndItems(_ as BrandItemManagementRequestDto) >> ResultResponseDto.success()
        itemUpdateResult.andExpect(status().isOk())
                .andExpect(jsonPath('$.data.result').value(true))

        when: "상품 삭제"
        def itemDeleteResult = mockMvc.perform(
                post("/v1/admin/brand-item-management")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "itemOperation": {
                                    "action": "DELETE",
                                    "itemId": 1
                                }
                            }
                        """)
        )
        then:
        1 * adminApplicationService.manageBrandAndItems(_ as BrandItemManagementRequestDto) >> ResultResponseDto.success()
        itemDeleteResult.andExpect(status().isOk())
                .andExpect(jsonPath('$.data.result').value(true))
    }
}
