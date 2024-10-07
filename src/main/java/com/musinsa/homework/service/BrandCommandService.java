package com.musinsa.homework.service;

import com.musinsa.homework.common.exception.ApiErrorCode;
import com.musinsa.homework.common.exception.ApiException;
import com.musinsa.homework.controller.request.BrandItemManagementRequestDto;
import com.musinsa.homework.controller.response.ResultResponseDto;
import com.musinsa.homework.entity.Brand;
import com.musinsa.homework.repository.BrandRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BrandCommandService {
    private final BrandRepository brandRepository;

    @Transactional
    public ResultResponseDto processBrandOperation(BrandItemManagementRequestDto.BrandOperation operation) {
        switch (operation.getAction()) {
            case CREATE:
                return createBrand(operation.getBrandName());
            default:
                throw new ApiException(ApiErrorCode.NOT_ALLOW_ACTION);
        }
    }

    private ResultResponseDto createBrand(String brandName) {
        if (brandRepository.existsByName(brandName)) {
            throw new ApiException(ApiErrorCode.DUPLICATED_BRAND_NAME);
        }

        Brand brand = new Brand(brandName);
        brandRepository.save(brand);
        return ResultResponseDto.success();
    }

}
