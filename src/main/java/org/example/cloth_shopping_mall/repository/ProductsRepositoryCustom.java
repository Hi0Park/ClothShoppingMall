package org.example.cloth_shopping_mall.repository;

import org.example.cloth_shopping_mall.entity.Category;
import org.example.cloth_shopping_mall.entity.ProductsEntity;

import java.util.List;

public interface ProductsRepositoryCustom {
    public List<ProductsEntity> findExtremumProductsByCategory(Category category);

}
