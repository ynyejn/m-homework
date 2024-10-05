package com.musinsa.homework.repository.item;


import com.musinsa.homework.entity.*;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.musinsa.homework.entity.QBrand.brand;
import static com.musinsa.homework.entity.QCategory.category;
import static com.musinsa.homework.entity.QItem.item;

@RequiredArgsConstructor
public class ItemRepositoryInterfaceImpl implements ItemRepositoryInterface {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Item> getLowestPricesByCategory() {
        //카테고리별로 최저가 찾아서 해당아이템 리스트 반환
        List<Item> items = queryFactory
                .selectFrom(item)
                .join(item.category, category).fetchJoin()
                .join(item.brand, brand).fetchJoin()
                .where(item.price.eq(
                        JPAExpressions
                                .select(item.price.min())
                                .from(item)
                                .where(item.category.eq(category))
                ))
                .fetch();

        return Optional.ofNullable(items).orElse(Collections.emptyList());
    }

    @Override
    public Brand getLowestPriceBrand() {
        //브랜드별로 최저가 찾아서 해당 브랜드 반환
        return queryFactory
                .select(item.brand)
                .from(item)
                .groupBy(item.brand.id)
                .orderBy(item.price.sum().asc())  //브랜드별로 가격합계를 구하고 오름차순 정렬
                .fetchFirst();
    }


}
