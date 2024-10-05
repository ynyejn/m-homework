package com.musinsa.homework;


import com.musinsa.homework.entity.Brand;
import com.musinsa.homework.entity.Category;
import com.musinsa.homework.entity.Item;
import com.musinsa.homework.repository.BrandRepository;
import com.musinsa.homework.repository.CatogoryRepository;
import com.musinsa.homework.repository.item.ItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class ApplicationRunner {
    @Bean
    @Transactional
    public CommandLineRunner initData(BrandRepository brandRepository, CatogoryRepository categoryRepository, ItemRepository itemRepository) {
        return args -> {
            List<Category> categories = new ArrayList<>();
            List<Brand> brands = new ArrayList<>();
            List<Item> items = new ArrayList<>();

            // 카테고리와 브랜드 생성
            categories.add(new Category("상의"));
            categories.add(new Category("아우터"));
            categories.add(new Category("바지"));
            categories.add(new Category("스니커즈"));
            categories.add(new Category("가방"));
            categories.add(new Category("모자"));
            categories.add(new Category("양말"));
            categories.add(new Category("액세서리"));

            brands.add(new Brand("A"));
            brands.add(new Brand("B"));
            brands.add(new Brand("C"));
            brands.add(new Brand("D"));
            brands.add(new Brand("E"));
            brands.add(new Brand("F"));
            brands.add(new Brand("G"));
            brands.add(new Brand("H"));
            brands.add(new Brand("I"));

            categoryRepository.saveAll(categories);
            brandRepository.saveAll(brands);

            // 각 브랜드별 카테고리별 가격 생성
            int[][] prices = {
                    {11200, 5500, 4200, 9000, 2000, 1700, 1800, 2300}, // A 브랜드
                    {10500, 5900, 3800, 9100, 2100, 2000, 2000, 2200}, // B 브랜드
                    {10000, 6200, 3300, 9200, 2200, 1900, 2200, 2100}, // C 브랜드
                    {10100, 5100, 3000, 9500, 2500, 1500, 2400, 2000}, // D 브랜드
                    {10700, 5000, 3800, 9900, 2300, 1800, 2100, 2100}, // E 브랜드
                    {11200, 7200, 4000, 9300, 2100, 1600, 2300, 1900}, // F 브랜드
                    {10500, 5800, 3900, 9000, 2200, 1700, 2100, 2000}, // G 브랜드
                    {10800, 6300, 3100, 9700, 2100, 1600, 2000, 2000}, // H 브랜드
                    {11400, 6700, 3200, 9500, 2400, 1700, 1700, 2400}  // I 브랜드
            };

            // 모든 브랜드와 카테고리에 대해 제품을 생성
            for (int i = 0; i < brands.size(); i++) {
                for (int j = 0; j < categories.size(); j++) {
                    items.add(new Item(brands.get(i), categories.get(j), prices[i][j]));
                }
            }
            itemRepository.saveAll(items);
        };
    }
}