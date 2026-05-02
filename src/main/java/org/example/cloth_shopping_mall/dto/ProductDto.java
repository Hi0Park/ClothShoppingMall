package org.example.cloth_shopping_mall.dto;

import org.example.cloth_shopping_mall.entity.ProductsEntity;

import java.util.List;

public class ProductDto {
    public record ProductsItemInfo(
            String brandName,
            String categoryName,
            Integer price
    ) {
        public static ProductsItemInfo from(ProductsEntity entity) {
            return new ProductsItemInfo(
                    entity.getBrandEntity().getName(),
                    entity.getCategory().name(),
                    entity.getPrice());
        }
    }
    public record ProductsResponse(
            List<ProductsItemInfo> items,
            Integer totalCost
    ) {
        public static ProductsResponse of(List<ProductsItemInfo> items) {
            Integer sum = items.stream()
                    .mapToInt(ProductsItemInfo::price)
                    .sum();
            return new ProductsResponse(items, sum);
        }
    }

    // 추가 예정
    public record ProductsRequest(
    ) { }
}
