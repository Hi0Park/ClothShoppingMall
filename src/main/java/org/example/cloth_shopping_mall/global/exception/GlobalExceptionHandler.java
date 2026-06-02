package org.example.cloth_shopping_mall.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.example.cloth_shopping_mall.domain.product.exception.ProductException;
import org.example.cloth_shopping_mall.global.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ProductException.class)
    public ResponseEntity<ApiResponse<Void>> handleProductException(ProductException e) {
        ErrorCode errorCode = e.getErrorCode();

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ApiResponse.error(e.getMessage(), e.getErrorCode().getCode()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        Class<?> requiredType = e.getRequiredType();

        if (requiredType != null && requiredType.isEnum()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("올바르지 않은 카테고리입니다.", "G001"));
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("입력된 데이터 타입이 올바르지 않습니다.", "G002"));
    }

    // 위로 예외 처리 핸들러 추가
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handlerGeneral(Exception e) {
        log.error("예상치 못한 예외 발생", e); // 스택 트레이스 로깅

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("INTERNAL_SERVER_ERROR", "SERVER_ERROR"));
    }
}
