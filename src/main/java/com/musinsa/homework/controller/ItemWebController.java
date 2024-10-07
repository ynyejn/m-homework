package com.musinsa.homework.controller;

import com.musinsa.homework.controller.response.BrandDto;
import com.musinsa.homework.entity.Brand;
import com.musinsa.homework.entity.Category;
import com.musinsa.homework.entity.Item;
import com.musinsa.homework.service.BrandQueryService;
import com.musinsa.homework.service.ItemQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/page")
public class ItemWebController {
    private final ItemQueryService itemQueryService;
    private final BrandQueryService brandQueryService;
    @RequestMapping()
    public String Home() {
        return "home";
    }

    // 카테고리 별 최저가격
    @GetMapping("/items/lowest-prices")
    public String getLowestPricesByCategory() {
        return "lowestPricesByCategory";
    }

    // 단일 브랜드 최저가격
    @GetMapping("/items/lowest-price-brand")
    public String getLowestPriceBrand() {
    return "lowestPriceBrand";
    }

    // 카테고리별 최저/최고 가격 브랜드 조회
    @GetMapping("/items/categories/prices")
    public String getCategoryPriceRange(Model model) {
        return "categoryPriceRange";
    }

    // 상품 조회
    @GetMapping("/items")
    public String getItems(Model model) {
        List<BrandDto> brands = itemQueryService.getItemsGroupedByBrand();
        model.addAttribute("brands", brands);
        return "itemList";
    }

    // 상품 등록 폼
    @GetMapping("/items/new")
    public String getCreateItemForm(Model model) {
        List<Brand> brands = brandQueryService.findAll();
        List<Item> items = itemQueryService.findAll();
        List<Category> categories = items.stream()
                .map(Item::getCategory)
                .distinct()
                .toList();

        model.addAttribute("brands", brands);
        model.addAttribute("categories", categories);
        model.addAttribute("items", items);

        return "createItemForm";
    }
}
