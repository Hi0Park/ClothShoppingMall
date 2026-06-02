package org.example.cloth_shopping_mall.domain.product.exception;

import lombok.Getter;
import org.example.cloth_shopping_mall.global.exception.ErrorCode;

@Getter
public class ProductException extends RuntimeException {
    private final ErrorCode errorCode;

    protected ProductException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
