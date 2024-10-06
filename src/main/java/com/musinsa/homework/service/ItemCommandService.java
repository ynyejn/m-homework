package com.musinsa.homework.service;

import com.musinsa.homework.common.exception.ApiErrorCode;
import com.musinsa.homework.common.exception.ApiException;
import com.musinsa.homework.controller.request.BrandItemManagementRequestDto;
import com.musinsa.homework.controller.response.ResultResponseDto;
import com.musinsa.homework.entity.Brand;
import com.musinsa.homework.entity.Category;
import com.musinsa.homework.entity.Item;
import com.musinsa.homework.repository.BrandRepository;
import com.musinsa.homework.repository.CategoryRepository;
import com.musinsa.homework.repository.item.ItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemCommandService {

    private final ItemRepository itemRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public ResultResponseDto processItemOperation(BrandItemManagementRequestDto.ItemOperation operation) {
        switch (operation.getAction()) {
            case CREATE:
                return createItem(operation.getBrandId(), operation.getCategoryId(), operation.getPrice());
            case UPDATE:
                return updateItem(operation.getBrandId(), operation.getCategoryId(), operation.getItemId(), operation.getPrice());
            case DELETE:
                return deleteItem(operation.getItemId());
            default:
                throw new ApiException(ApiErrorCode.NOT_ALLOW_ACTION);
        }
    }

    private ResultResponseDto createItem(Long brandId, Long categoryId, int price) {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new ApiException(ApiErrorCode.BRAND_NOT_FOUND));
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ApiException(ApiErrorCode.CATEGORY_NOT_FOUND));

        Item item = new Item(brand, category, price);
        itemRepository.save(item);
        return ResultResponseDto.success();
    }

    private ResultResponseDto updateItem(Long brandId, Long categoryId, Long itemId, int price) {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new ApiException(ApiErrorCode.BRAND_NOT_FOUND));
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ApiException(ApiErrorCode.CATEGORY_NOT_FOUND));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ApiException(ApiErrorCode.ITEM_NOT_FOUND));

        item.updateItem(brand, category, price);
        return ResultResponseDto.success();
    }

    private ResultResponseDto deleteItem(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ApiException(ApiErrorCode.ITEM_NOT_FOUND));

        itemRepository.delete(item);
        return ResultResponseDto.success();
    }
}
