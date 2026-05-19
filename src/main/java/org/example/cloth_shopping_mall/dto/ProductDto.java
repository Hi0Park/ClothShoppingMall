package org.example.cloth_shopping_mall.dto;

import org.example.cloth_shopping_mall.entity.ProductsEntity;

import java.util.List;

public class ProductDto {
    public record ProductsForCategoryItemInfo(
            String brandName,
            String categoryName,
            Integer price
    ) {
        public static ProductsForCategoryItemInfo from(ProductsEntity entity) {
            return new ProductsForCategoryItemInfo(
                    entity.getBrandEntity().getName(),
                    entity.getCategory().name(),
                    entity.getPrice());
        }
    }
    public record ProductsForCategoryResponse(
            List<ProductsForCategoryItemInfo> items,
            Integer totalCost
    ) {
        public static ProductsForCategoryResponse of(List<ProductsForCategoryItemInfo> items) {
            Integer sum = items.stream()
                    .mapToInt(ProductsForCategoryItemInfo::price)
                    .sum();
            return new ProductsForCategoryResponse(items, sum);
        }
    }

    public record ProductsForOneBrandItemInfo(
            String categoryName,
            Integer price
    ) {
        public static ProductsForOneBrandItemInfo from(ProductsEntity entity) {
            return new ProductsForOneBrandItemInfo(
                    entity.getCategory().name(),
                    entity.getPrice());
        }
    }

    public record ProductsForOneBrandResponse(
            String brandName,
            List<ProductsForOneBrandItemInfo> items,
            Integer price
    ){
        public static ProductsForOneBrandResponse of(String brandName, List<ProductsForOneBrandItemInfo> items) {
            Integer sum = items.stream()
                    .mapToInt(ProductsForOneBrandItemInfo::price)
                    .sum();
            return new ProductsForOneBrandResponse(brandName, items, sum);
        }
    }

    // 추가 예정
    public record ProductsRequest(
    ) { }
}
