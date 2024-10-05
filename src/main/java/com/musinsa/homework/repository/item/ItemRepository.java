package com.musinsa.homework.repository.item;

import com.musinsa.homework.entity.Brand;
import com.musinsa.homework.entity.Item;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryInterface {
    @EntityGraph(attributePaths = {"category", "brand"})
    List<Item> findByBrand(Brand lowestPriceBrand);
}
