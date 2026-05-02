package org.example.cloth_shopping_mall.repository;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
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

    @Override
    public List<ProductsEntity> findMinPriceByCategory() {
        return queryFactory.selectFrom(productsEntity)
                .join(productsEntity.brandEntity).fetchJoin()
                .where(Expressions.list(productsEntity.category, productsEntity.price).in(
                        JPAExpressions
                                .select(subProductsEntity.category, subProductsEntity.price.min())
                                .from(subProductsEntity)
                                .groupBy(subProductsEntity.category)
                ))
                .fetch();
    }
}
