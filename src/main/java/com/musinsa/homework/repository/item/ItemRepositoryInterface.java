package com.musinsa.homework.repository.item;


import com.musinsa.homework.entity.Brand;
import com.musinsa.homework.entity.Item;

import java.util.List;


public interface ItemRepositoryInterface {
    List<Item> getLowestPricesByCategory();

    Brand getLowestPriceBrand();

    List<Item> findItemsWithExtremePrice(String categoryName, boolean isMinPrice);
}
