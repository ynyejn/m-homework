package com.musinsa.homework.service;

import com.musinsa.homework.controller.response.LowestPriceBrandResponseDto;
import com.musinsa.homework.controller.response.LowestPriceByCategoryResponseDto;
import com.musinsa.homework.entity.Brand;
import com.musinsa.homework.entity.Item;
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

    public List<Item> getLowestPricesByCategory() {
        return itemRepository.getLowestPricesByCategory()
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
                                .collect(Collectors.toList())
                ));
    }

    public Brand getLowestPriceBrand() {
        return itemRepository.getLowestPriceBrand();
    }

    public List<Item> getItemsByBrand(Brand brand) {
        return itemRepository.findByBrand(brand);
    }
}
