package org.example.cloth_shopping_mall.service;

import lombok.RequiredArgsConstructor;
import org.example.cloth_shopping_mall.dto.ProductDto;
import org.example.cloth_shopping_mall.entity.BrandCategoryLowestPriceEntity;
import org.example.cloth_shopping_mall.entity.Category;
import org.example.cloth_shopping_mall.entity.ProductsEntity;
import org.example.cloth_shopping_mall.repository.BrandCategoryALowestPriceRepository;
import org.example.cloth_shopping_mall.repository.BrandRepository;
import org.example.cloth_shopping_mall.repository.ProductsRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final ProductsRepository productsRepository;
    private final BrandCategoryALowestPriceRepository lowestPriceRepository;

    // 1. 카테고리별 최저가격 브랜드 & 상품 가격 & 총액 조회 API
    @Transactional(readOnly = true)
    @Cacheable(value = "lowestPriceEachCategory")
    public ProductDto.ProductsForCategoryResponse findLowestPriceWithEachCategory() {

        // 2. 무거운 상품 테이블 대신, 가벼운 장부 테이블을 통째로 긁어옵니다. (데이터 정합성이 이미 맞춰진 상태)
        List<BrandCategoryLowestPriceEntity> allLowestPrices = lowestPriceRepository.findAll();

        // 3. 카테고리 이름별로 그룹핑합니다.
        Map<String, List<BrandCategoryLowestPriceEntity>> grouped = allLowestPrices.stream()
                .collect(Collectors.groupingBy(BrandCategoryLowestPriceEntity::getCategoryName));

        List<ProductDto.ProductsForCategoryItemInfo> finalItems = new ArrayList<>();
        Set<String> pickedBrands = new HashSet<>();

        // 4. 선우님이 짜두신 브랜드 분산 알고리즘을 그대로 적용합니다.
        grouped.forEach((categoryName, brandPrices) -> {
            BrandCategoryLowestPriceEntity selected = brandPrices.stream()
                    .min(Comparator
                            // 컬럼 순서 A: 일단 가격이 가장 절대적으로 낮아야 함!
                            .comparingInt(BrandCategoryLowestPriceEntity::getPrice)
                            // 컬럼 순서 B: 가격이 같다면, 이미 뽑힌 브랜드는 뒤로 밀기 (선우님의 기존 로직 🎯)
                            .thenComparingInt((BrandCategoryLowestPriceEntity e) -> pickedBrands.contains(e.getBrandName()) ? 1 : 0)
                            // 컬럼 순서 C: 그래도 같다면 브랜드 이름 순 정렬
                            .thenComparing(BrandCategoryLowestPriceEntity::getBrandName)
                    ).orElseThrow();

            pickedBrands.add(selected.getBrandName());

            // 5. 변환된 데이터를 DTO 리스트에 담아줍니다.
            finalItems.add(new ProductDto.ProductsForCategoryItemInfo(
                    selected.getCategoryName(),
                    selected.getBrandName(),
                    selected.getPrice()
            ));
        });

        return ProductDto.ProductsForCategoryResponse.of(finalItems);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "lowestBrand")
    public ProductDto.ProductsForOneBrandResponse findLowestPriceWithOneBrand() {
        List<BrandCategoryLowestPriceEntity> allLowestPrices = lowestPriceRepository.findAll();

        if (allLowestPrices.isEmpty()) {
            throw new IllegalArgumentException("현재 등록된 상품 데이터가 없습니다."); // TODO : 예외 처리 변경
        }

        String lowestBrandName = allLowestPrices.stream()
                .collect(Collectors.groupingBy(
                        BrandCategoryLowestPriceEntity::getBrandName,
                        Collectors.summingInt(BrandCategoryLowestPriceEntity::getPrice)
                ))
                .entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElseThrow();

        List<ProductDto.ProductsWithCategoryNameAndPrice> itemInfos = allLowestPrices.stream()
                .filter(row -> row.getBrandName().equals(lowestBrandName))
                .map(row -> new ProductDto.ProductsWithCategoryNameAndPrice(
                        row.getCategoryName(),
                        row.getPrice()
                ))
                .toList();

        return ProductDto.ProductsForOneBrandResponse.of(lowestBrandName, itemInfos);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "extremumProducts")
    public ProductDto.ProductsForExtremumWithCategoryResponse findExtremumProductsWithCategory(String categoryName) {
        List<ProductsEntity> extremumProducts = productsRepository.findExtremumProductsByCategory(Category.valueOf(categoryName.toUpperCase()));
        /*
        TODO : 예외 처리 분리
            1. ENUM에 존재하지 않는 Category
            2. 상품 미존재
         */
        if (extremumProducts.isEmpty()) {
            throw new IllegalArgumentException("상품이 존재하지 않습니다");
        }

        int maxPrice = extremumProducts.stream()
                .mapToInt(ProductsEntity::getPrice)
                .max()
                .orElseThrow();
        int minPrice = extremumProducts.stream()
                .mapToInt(ProductsEntity::getPrice)
                .min()
                .orElseThrow();

        List<ProductDto.ProductsWithBrandNameAndPrice> minPriceProducts = extremumProducts.stream()
                .filter(productsEntity -> productsEntity.getPrice() == minPrice)
                .map(row -> new ProductDto.ProductsWithBrandNameAndPrice(
                        row.getBrandEntity().getName(),
                        row.getPrice()
                ))
                .toList();

        List<ProductDto.ProductsWithBrandNameAndPrice> maxPriceProducts = extremumProducts.stream()
                .filter(productsEntity -> productsEntity.getPrice() == maxPrice)
                .map(row -> new ProductDto.ProductsWithBrandNameAndPrice(
                        row.getBrandEntity().getName(),
                        row.getPrice()
                ))
                .toList();

        return ProductDto.ProductsForExtremumWithCategoryResponse.of(categoryName, minPriceProducts, maxPriceProducts);
    }
}
