package org.example.cloth_shopping_mall.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor

public class BrandCategoryLowestPriceEntity {
    // 추후에 SQL 파일 제작 해야함

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String brandName;
    private String categoryName;
    private Integer price;
    private Long productId;

    public BrandCategoryLowestPriceEntity(String brandName, String categoryName, Integer price, Long productId) {
        this.brandName = brandName;
        this.categoryName = categoryName;
        this.price = price;
        this.productId = productId;
    }

    public void updateLowestPrice(Long productId, Integer price) {
        this.productId = productId;
        this.price = price;
    }
}
