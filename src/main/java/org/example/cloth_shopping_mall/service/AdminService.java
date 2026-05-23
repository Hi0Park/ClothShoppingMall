package org.example.cloth_shopping_mall.service;

import lombok.RequiredArgsConstructor;
import org.example.cloth_shopping_mall.dto.ProductDto;
import org.example.cloth_shopping_mall.entity.BrandCategoryLowestPriceEntity;
import org.example.cloth_shopping_mall.entity.ProductsEntity;
import org.example.cloth_shopping_mall.repository.BrandCategoryALowestPriceRepository;
import org.example.cloth_shopping_mall.repository.BrandRepository;
import org.example.cloth_shopping_mall.repository.ProductsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final ProductsRepository productsRepository;
    private final BrandCategoryALowestPriceRepository lowestPriceRepository;
    private final BrandRepository brandRepository;

    @Transactional
    public Long addProduct(ProductDto.AddProductRequest request) {
        ProductsEntity product = request.toEntity(
                brandRepository.findByName(request.brandName().toUpperCase())
        );

        ProductsEntity newProduct = productsRepository.save(product);

        syncLowestPrice(product);

        return newProduct.getId();
    }

    public void syncLowestPrice(ProductsEntity product) {
        String brandName = product.getBrandEntity().getName();
        String categoryName = product.getCategory().name();

        BrandCategoryLowestPriceEntity currentLowestProduct = lowestPriceRepository.findByBrandNameAndCategoryName(brandName, categoryName);
        if (currentLowestProduct == null) {
            lowestPriceRepository.save(new BrandCategoryLowestPriceEntity(
                    brandName,
                    categoryName,
                    product.getPrice(),
                    product.getId()
            ));
        } else if (product.getPrice() < currentLowestProduct.getPrice()) {
            currentLowestProduct.updateLowestPrice(product.getId(), product.getPrice());
        } else if (product.getId().equals(currentLowestProduct.getProductId())) {
            ProductsEntity nextLowestPriceProduct = productsRepository
                    .findFirstByBrandEntityAndCategoryAndDeletedAtIsNullOrderByPriceAsc(product.getBrandEntity(), product.getCategory());

            currentLowestProduct.updateLowestPrice(nextLowestPriceProduct.getId(), nextLowestPriceProduct.getPrice());
        }
    }

    @Transactional
    public void deleteProduct(Long productId) {
        ProductsEntity product = productsRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다.")); // TODO : 예외 처리 변경

        product.updateDeletedAt(LocalDateTime.now());

        String brandName = product.getBrandEntity().getName();
        String categoryName = product.getCategory().name();
        BrandCategoryLowestPriceEntity lowestPriceProduct = lowestPriceRepository.findByBrandNameAndCategoryName(brandName, categoryName);

        if (lowestPriceProduct != null && lowestPriceProduct.getProductId().equals(productId)) {
            ProductsEntity nextLowestPriceProduct = productsRepository
                    .findFirstByBrandEntityAndCategoryAndDeletedAtIsNullOrderByPriceAsc(product.getBrandEntity(), product.getCategory());

            if (nextLowestPriceProduct != null) {
                lowestPriceProduct.updateLowestPrice(nextLowestPriceProduct.getId(), nextLowestPriceProduct.getPrice());
            } else {
                lowestPriceRepository.delete(lowestPriceProduct);
            }
        }
    }

    @Transactional
    public Long updateProduct(ProductDto.UpdateProductRequest request) {
        ProductsEntity product = productsRepository.findById(request.id())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품 ID입니다")); // TODO : 예외 처리 변경

        product.updateProduct(request.price());
        syncLowestPrice(product);

        return request.id();
    }
}
