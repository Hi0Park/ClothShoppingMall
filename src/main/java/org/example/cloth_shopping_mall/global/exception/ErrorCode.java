package org.example.cloth_shopping_mall.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    BRAND_NOT_FOUND(HttpStatus.NOT_FOUND, "B001", "해당 브랜드를 찾을 수 없습니다."),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "P001", "해당 상품을 찾을 수 없습니다."),
    NO_PRODUCT_EXIST(HttpStatus.NOT_FOUND, "P002", "현재 시스템에 등록된 상품 데이터가 존재하지 않습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
