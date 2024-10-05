package com.musinsa.homework.controller.response;


import com.musinsa.homework.common.util.PriceFormatter;
import com.musinsa.homework.entity.Item;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "카테고리별 최저가 상품정보")
public class LowestPriceByCategoryResponseDto {
    @Schema(description = "상품목록")
    private List<ItemResponseDto> items;
    @Schema(description = "총 가격", example = "100,000")
    private String totalPrice;

    public LowestPriceByCategoryResponseDto(List<ItemResponseDto> items, String totalPrice) {
        this.items = items;
        this.totalPrice = totalPrice;
    }

    public static LowestPriceByCategoryResponseDto from(List<Item> items) {
        if (items==null || items.isEmpty()) {
            return new LowestPriceByCategoryResponseDto(List.of(), "0");
        }
        return new LowestPriceByCategoryResponseDto(items.stream().map(ItemResponseDto::from).toList(), PriceFormatter.format(items.stream().mapToInt(Item::getPrice).sum()));
    }
}
