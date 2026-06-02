package org.example.cloth_shopping_mall.domain.product.repository;

import org.example.cloth_shopping_mall.domain.product.entity.Category;
import org.example.cloth_shopping_mall.domain.product.entity.ProductsEntity;

import java.util.List;

public interface ProductsRepositoryCustom {
    public List<ProductsEntity> findExtremumProductsByCategory(Category category);

}
