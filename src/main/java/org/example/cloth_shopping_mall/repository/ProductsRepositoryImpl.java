package org.example.cloth_shopping_mall.repository;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.cloth_shopping_mall.entity.Category;
import org.example.cloth_shopping_mall.entity.ProductsEntity;
import org.example.cloth_shopping_mall.entity.QProductsEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductsRepositoryImpl implements ProductsRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    QProductsEntity productsEntity = QProductsEntity.productsEntity;
    QProductsEntity subProductsEntity = new QProductsEntity("sub");

    @SuppressWarnings("unchecked")
    @Override
    public List<ProductsEntity> findExtremumProductsByCategory(Category category) {
        return queryFactory.selectFrom(productsEntity)
                .join(productsEntity.brandEntity).fetchJoin()
                .where(
                        productsEntity.category.eq(category)
                                .and(
                                        productsEntity.price.in(
                                                JPAExpressions
                                                        .select(subProductsEntity.price.min())
                                                        .from(subProductsEntity)
                                                        .where(subProductsEntity.category.eq(category)),
                                                JPAExpressions
                                                        .select(subProductsEntity.price.max())
                                                        .from(subProductsEntity)
                                                        .where(subProductsEntity.category.eq(category))
                                        )
                                )
                )
                .fetch();
    }


}
