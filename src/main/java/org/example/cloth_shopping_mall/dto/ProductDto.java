package org.example.cloth_shopping_mall.dto;

import org.example.cloth_shopping_mall.entity.ProductsEntity;

import java.util.List;

public class ProductDto {
    public record ProductsForCategoryItemInfo(
            String brandName,
            String categoryName,
            Integer price
    ) { }
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

    public record ProductsWithCategoryNameAndPrice(
            String categoryName,
            Integer price
    ) { }
    public record ProductsForOneBrandResponse(
            String brandName,
            List<ProductsWithCategoryNameAndPrice> items,
            Integer price
    ){
        public static ProductsForOneBrandResponse of(String brandName, List<ProductsWithCategoryNameAndPrice> items) {
            Integer sum = items.stream()
                    .mapToInt(ProductsWithCategoryNameAndPrice::price)
                    .sum();
            return new ProductsForOneBrandResponse(brandName, items, sum);
        }
    }

    public record ProductsWithBrandNameAndPrice(
        String brandName,
        Integer price
    ) { }
    public record ProductsForExtremumWithCategoryResponse(
            String categoryName,
            List<ProductsWithBrandNameAndPrice> minPriceProducts,
            List<ProductsWithBrandNameAndPrice> maxPriceProducts
    ) {
        public static ProductsForExtremumWithCategoryResponse of(
                String categoryName,
                List<ProductsWithBrandNameAndPrice> minPriceProducts,
                List<ProductsWithBrandNameAndPrice> maxPriceProducts
        ) {
            return new ProductsForExtremumWithCategoryResponse(categoryName, minPriceProducts, maxPriceProducts);
        }
    }

    // 추가 예정
    public record ProductsRequest(
    ) { }
}
