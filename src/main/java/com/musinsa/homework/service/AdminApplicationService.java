package com.musinsa.homework.service;

import com.musinsa.homework.controller.request.BrandItemManagementRequestDto;
import com.musinsa.homework.controller.response.ResultResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminApplicationService {

    private final BrandCommandService brandService;
    private final ItemCommandService itemService;

    @Transactional
    public ResultResponseDto manageBrandAndItems(BrandItemManagementRequestDto request) {
        ResultResponseDto result = null;
        if (request.getBrandOperation() != null) {
            result = brandService.processBrandOperation(request.getBrandOperation());
        } else if (request.getItemOperation() != null)
            result = itemService.processItemOperation(request.getItemOperation());
        return result;
    }
}
