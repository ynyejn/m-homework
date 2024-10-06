package com.musinsa.homework.repository;

import com.musinsa.homework.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    boolean existsByName(String brandName);

    Optional<Brand> findByName(String brandName);
}
