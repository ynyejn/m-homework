package com.musinsa.homework.repository;

import com.musinsa.homework.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatogoryRepository extends JpaRepository<Category, Long> {
}
