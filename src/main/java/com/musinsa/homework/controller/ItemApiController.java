package com.musinsa.homework.controller;

import com.musinsa.homework.controller.response.ApiResponseWrapper;
import com.musinsa.homework.controller.response.CategoryPriceRangeResponseDto;
import com.musinsa.homework.controller.response.LowestPriceBrandResponseDto;
import com.musinsa.homework.controller.response.LowestPriceByCategoryResponseDto;
import com.musinsa.homework.service.ItemQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/items")
@Tag(name = "items", description = "items api")
public class ItemApiController {
    private final ItemQueryService itemQueryService;

    @GetMapping("/lowest-prices")
    @Operation(summary = "카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회", description = "1. 고객은 카테고리 별로 최저가격인 브랜드와 가격을 조회하고 총액이 얼마인지 확인할 수 있어야 합니다.")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ApiResponseWrapper.class)))
    @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = ApiResponseWrapper.WithError.class)))
    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(implementation = ApiResponseWrapper.WithError.class)))
    public ResponseEntity<ApiResponseWrapper<LowestPriceByCategoryResponseDto>> getLowestPricesByCategory() {
        LowestPriceByCategoryResponseDto response = itemQueryService.getLowestPricesByCategory();
        return ResponseEntity.ok(ApiResponseWrapper.success(response));
    }

    @GetMapping("/lowest-price-brand")
    @Operation(summary = "단일 브랜드 최저가격 조회",
            description = "2. 고객은 단일 브랜드로 전체 카테고리 상품을 구매할 경우 최저가격인 브랜드와 총액이 얼마인지 확인할 수 있어야 합니다.")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ApiResponseWrapper.class)))
    @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = ApiResponseWrapper.WithError.class)))
    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(implementation = ApiResponseWrapper.WithError.class)))
    public ResponseEntity<ApiResponseWrapper<LowestPriceBrandResponseDto>> getLowestPriceBrand() {
        LowestPriceBrandResponseDto response = itemQueryService.getLowestPriceBrand();
        return ResponseEntity.ok(ApiResponseWrapper.success(response));
    }

    @GetMapping("/categories/{categoryName}/prices")
    @Operation(summary = "카테고리별 최저/최고 가격 브랜드 조회",
            description = "3. 고객은 특정 카테고리에서 최저가격 브랜드와 최고가격 브랜드를 확인하고 각 브랜드 상품의 가격을 확인할 수 있어야 합니다.")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ApiResponseWrapper.class)))
    @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = ApiResponseWrapper.WithError.class)))
    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(implementation = ApiResponseWrapper.WithError.class)))
    public ResponseEntity<ApiResponseWrapper<CategoryPriceRangeResponseDto>> getCategoryPriceRange(
            @Parameter(description = "카테고리 명", required = true) @PathVariable String categoryName) {
        return ResponseEntity.ok(ApiResponseWrapper.success(itemQueryService.getCategoryPriceRange(categoryName)));
    }

}
