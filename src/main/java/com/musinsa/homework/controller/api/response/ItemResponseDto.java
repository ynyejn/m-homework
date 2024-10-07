package com.musinsa.homework.controller.api.response;

import com.musinsa.homework.entity.Item;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.text.NumberFormat;
import java.util.Locale;

@Getter
@Schema(description = "상품정보")
public class ItemResponseDto {
    @Schema(description = "브랜드", example = "나이키")
    private String brand;
    @Schema(description = "카테고리", example = "신발")
    private String category;
    @Schema(description = "가격", example = "100,000")
    private String price;

    private ItemResponseDto(String brand, String category, String price) {
        this.brand = brand;
        this.category = category;
        this.price = price;
    }


    public static ItemResponseDto from(Item item) {
        return new ItemResponseDto(item.getBrand().getName(), item.getCategory().getName(), NumberFormat.getNumberInstance(Locale.US).format(item.getPrice()));
    }
}
