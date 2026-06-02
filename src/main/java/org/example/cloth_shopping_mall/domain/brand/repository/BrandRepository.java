package org.example.cloth_shopping_mall.domain.brand.repository;

import org.example.cloth_shopping_mall.domain.brand.entity.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<BrandEntity, Long> {
    BrandEntity findByName(String name);
}
