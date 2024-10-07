package com.musinsa.homework.controller.response;

import java.util.Map;

public class BrandDto {
    private String name;
    private Map<String, String> prices;

    public BrandDto(String name, Map<String, String> prices) {
        this.name = name;
        this.prices = prices;
    }

    public String getName() {
        return name;
    }

    public Map<String, String> getPrices() {
        return prices;
    }
}
