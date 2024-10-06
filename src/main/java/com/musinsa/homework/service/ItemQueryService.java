package com.musinsa.homework.service;

import com.musinsa.homework.common.exception.ApiErrorCode;
import com.musinsa.homework.common.exception.ApiException;
import com.musinsa.homework.controller.response.CategoryPriceRangeResponseDto;
import com.musinsa.homework.controller.response.LowestPriceBrandResponseDto;
import com.musinsa.homework.controller.response.LowestPriceByCategoryResponseDto;
import com.musinsa.homework.entity.Brand;
import com.musinsa.homework.entity.Category;
import com.musinsa.homework.entity.Item;
import com.musinsa.homework.repository.CategoryRepository;
import com.musinsa.homework.repository.item.ItemRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemQueryService {
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;

    public LowestPriceByCategoryResponseDto getLowestPricesByCategory() {
        List<Item> lowestPriceItemByCategory =  itemRepository.getLowestPricesByCategory()
                .stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(
                                Item::getCategory,
                                item -> item,
                                (existing, replacement) -> replacement // 동일한 카테고리의 경우 덮어씌움
                        ),
                        // 카테고리 ID로 정렬하여 반환
                        map -> map.values().stream()
                                .sorted(Comparator.comparing(item -> item.getCategory().getId()))
                                .toList()
                ));
        return LowestPriceByCategoryResponseDto.from(lowestPriceItemByCategory);
    }

    public LowestPriceBrandResponseDto getLowestPriceBrand() {
        Brand lowestPriceBrand = itemRepository.getLowestPriceBrand();
        if (lowestPriceBrand == null) {
            return LowestPriceBrandResponseDto.empty();
        }
        List<Item> items = itemRepository.findByBrand(lowestPriceBrand);
        return LowestPriceBrandResponseDto.from(items);
    }

    public CategoryPriceRangeResponseDto getCategoryPriceRange(String categoryName) {
        Category category = categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new ApiException(ApiErrorCode.CATEGORY_NOT_FOUND));
        List<Item> itemsWithMinPrice = itemRepository.findItemsWithExtremePrice(category.getName(), true);
        List<Item> itemsWithMaxPrice = itemRepository.findItemsWithExtremePrice(category.getName(), false);
        return CategoryPriceRangeResponseDto.of(categoryName, itemsWithMinPrice, itemsWithMaxPrice);
    }
}
