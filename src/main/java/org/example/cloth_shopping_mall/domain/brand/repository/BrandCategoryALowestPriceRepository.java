package org.example.cloth_shopping_mall.domain.brand.repository;

import org.example.cloth_shopping_mall.domain.brand.entity.BrandCategoryLowestPriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandCategoryALowestPriceRepository extends JpaRepository<BrandCategoryLowestPriceEntity, Long> {
    BrandCategoryLowestPriceEntity findByBrandNameAndCategoryName(String brandName, String categoryName);

    void deleteByBrandName(String brandName);

}
