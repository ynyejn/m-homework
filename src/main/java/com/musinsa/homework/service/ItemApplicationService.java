package com.musinsa.homework.service;


import com.musinsa.homework.controller.response.LowestPriceBrandResponseDto;
import com.musinsa.homework.controller.response.LowestPriceByCategoryResponseDto;
import com.musinsa.homework.entity.Brand;
import com.musinsa.homework.entity.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ItemApplicationService {
    private final ItemQueryService itemQueryService;
    private final ItemCommandService itemCommandService;

    public LowestPriceByCategoryResponseDto getLowestPricesByCategory() {
        return LowestPriceByCategoryResponseDto.from(itemQueryService.getLowestPricesByCategory());

    }
    public LowestPriceBrandResponseDto getLowestPriceBrand() {
        Brand lowestPriceBrand = itemQueryService.getLowestPriceBrand();
        if (lowestPriceBrand == null) {
            return LowestPriceBrandResponseDto.empty();
        }
        List<Item> items = itemQueryService.getItemsByBrand(lowestPriceBrand);
        return LowestPriceBrandResponseDto.from(items);
    }
}
