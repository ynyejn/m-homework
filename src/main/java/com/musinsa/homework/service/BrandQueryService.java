package com.musinsa.homework.service;

import com.musinsa.homework.entity.Brand;
import com.musinsa.homework.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BrandQueryService {
    private final BrandRepository brandRepository;

    public List<Brand> findAll() {
        return brandRepository.findAll();
    }
}
