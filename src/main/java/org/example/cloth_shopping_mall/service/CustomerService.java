package org.example.cloth_shopping_mall.service;

import lombok.RequiredArgsConstructor;
import org.example.cloth_shopping_mall.dto.ProductDto;
import org.example.cloth_shopping_mall.entity.Category;
import org.example.cloth_shopping_mall.entity.ProductsEntity;
import org.example.cloth_shopping_mall.repository.BrandRepository;
import org.example.cloth_shopping_mall.repository.ProductsRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final BrandRepository brandRepository;
    private final ProductsRepository productsRepository;

    // 1. 카테고리별 최저가격 브랜드 & 상품 가격 & 총액 조회 API
    public ProductDto.ProductsResponse findEachCategoryWithLowestPrice() {
        List<ProductsEntity> entities = productsRepository.findMinPriceByCategory();

        Map<Category, List<ProductsEntity>> grouped = entities.stream()
                .collect(Collectors.groupingBy(ProductsEntity::getCategory));

        List<ProductDto.ProductsItemInfo> finalItems = new ArrayList<>();
        Set<String> pickedBrands = new HashSet<>();

        grouped.forEach((category, productsEntities) -> {
            ProductsEntity selected = productsEntities.stream()
                    .min(Comparator
                            .comparingInt((ProductsEntity e) -> pickedBrands.contains(e.getBrandEntity().getName()) ? 1 : 0)
                            .thenComparing(e -> e.getBrandEntity().getName())
                    ).orElseThrow();
            pickedBrands.add(selected.getBrandEntity().getName());
            finalItems.add(ProductDto.ProductsItemInfo.from(selected));
        });

        return ProductDto.ProductsResponse.of(finalItems);
    }

    public ProductDto.ProductsResponse findLowestBrandWithItems() {

    }


}
