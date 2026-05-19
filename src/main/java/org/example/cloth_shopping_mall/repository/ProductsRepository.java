package org.example.cloth_shopping_mall.repository;

import org.example.cloth_shopping_mall.entity.BrandEntity;
import org.example.cloth_shopping_mall.entity.Category;
import org.example.cloth_shopping_mall.entity.ProductsEntity;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductsRepository extends JpaRepository<ProductsEntity, Long>, ProductsRepositoryCustom {
    List<ProductsEntity> findByCategory(Category category, Sort sort, Limit limit);

    ProductsEntity findFirstByBrandEntityAndCategoryAndDeletedAtIsNullOrderByPriceAsc(BrandEntity brandEntity, Category category);
}
