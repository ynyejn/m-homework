package com.musinsa.homework.controller.api.response;

import com.musinsa.homework.common.util.PriceFormatter;
import com.musinsa.homework.entity.Item;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@Schema(description = "카테고리별 최저/최고 가격 브랜드 응답")
public class CategoryPriceRangeResponseDto {

    @Schema(description = "카테고리")
    private String 카테고리;

    @Schema(description = "최저가 정보")
    private List<PriceBrandInfo> 최저가;

    @Schema(description = "최고가 정보")
    private List<PriceBrandInfo> 최고가;

    @Getter
    @Builder
    public static class PriceBrandInfo {
        @Schema(description = "브랜드")
        private String 브랜드;

        @Schema(description = "가격")
        private String 가격;
    }

    public static CategoryPriceRangeResponseDto of(String categoryName, List<Item> itemsWithMinPrice, List<Item> itemsWithMaxPrice) {
        return CategoryPriceRangeResponseDto.builder()
                .카테고리(categoryName)
                .최저가(convertToPriceBrandInfoList(itemsWithMinPrice))
                .최고가(convertToPriceBrandInfoList(itemsWithMaxPrice))
                .build();
    }

    private static List<PriceBrandInfo> convertToPriceBrandInfoList(List<Item> items) {
        return items.stream()
                .map(item -> PriceBrandInfo.builder()
                        .브랜드(item.getBrand().getName())
                        .가격(PriceFormatter.format(item.getPrice()))
                        .build())
                .collect(Collectors.toList());
    }
}