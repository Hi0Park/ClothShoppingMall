package org.example.cloth_shopping_mall.repository;

import org.example.cloth_shopping_mall.entity.BrandCategoryLowestPriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandCategoryALowestPriceRepository extends JpaRepository<BrandCategoryLowestPriceEntity, Long> {
    BrandCategoryLowestPriceEntity findByBrandNameAndCategoryName(String brandName, String categoryName);

    void deleteByBrandName(String brandName);

}
