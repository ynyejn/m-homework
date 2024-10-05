package com.musinsa.homework.controller.response;

import com.musinsa.homework.common.util.PriceFormatter;
import com.musinsa.homework.entity.Item;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@Schema(description = "단일 브랜드 최저가격 응답")
public class LowestPriceBrandResponseDto {

    @Schema(description = "최저가")
    private LowestPriceInfo 최저가;

    @Getter
    @Builder
    public static class LowestPriceInfo {
        @Schema(description = "브랜드명", example = "D")
        private String 브랜드;

        @Schema(description = "카테고리별 가격 정보")
        private List<CategoryPrice> 카테고리;

        @Schema(description = "총액", example = "36,100")
        private String 총액;
    }

    @Getter
    @Builder
    public static class CategoryPrice {
        @Schema(description = "카테고리명", example = "상의")
        private String 카테고리;

        @Schema(description = "가격", example = "10,100")
        private String 가격;
    }

    public static LowestPriceBrandResponseDto from(List<Item> items) {
        if (items.isEmpty()) {
            return empty();
        }

        String brandName = items.get(0).getBrand().getName();
        List<CategoryPrice> categoryPrices = createCategoryPrices(items);
        String totalPrice = calculateTotalPrice(items);

        return LowestPriceBrandResponseDto.builder()
                .최저가(LowestPriceInfo.builder()
                        .브랜드(brandName)
                        .카테고리(categoryPrices)
                        .총액(totalPrice)
                        .build())
                .build();
    }

    private static List<CategoryPrice> createCategoryPrices(List<Item> items) {
        return items.stream()
                .map(LowestPriceBrandResponseDto::createCategoryPrice)
                .toList();
    }

    private static CategoryPrice createCategoryPrice(Item item) {
        return CategoryPrice.builder()
                .카테고리(item.getCategory().getName())
                .가격(PriceFormatter.format(item.getPrice()))
                .build();
    }

    private static String calculateTotalPrice(List<Item> items) {
        int total = items.stream()
                .mapToInt(Item::getPrice)
                .sum();
        return PriceFormatter.format(total);
    }

    public static LowestPriceBrandResponseDto empty() {
        return builder()
                .최저가(LowestPriceInfo.builder()
                        .브랜드("")
                        .카테고리(new ArrayList<>())
                        .총액("0")
                        .build())
                .build();
    }
}