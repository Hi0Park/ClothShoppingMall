package org.example.cloth_shopping_mall.domain.product.exception;

import org.example.cloth_shopping_mall.global.exception.ErrorCode;

public class ProductNotFoundException extends ProductException {
    public ProductNotFoundException() {
        super(ErrorCode.PRODUCT_NOT_FOUND);
    }
}
