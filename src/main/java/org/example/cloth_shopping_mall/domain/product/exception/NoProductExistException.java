package org.example.cloth_shopping_mall.domain.product.exception;

import org.example.cloth_shopping_mall.global.exception.ErrorCode;

public class NoProductExistException extends ProductException {
    public NoProductExistException() {
        super(ErrorCode.NO_PRODUCT_EXIST);
    }
}
