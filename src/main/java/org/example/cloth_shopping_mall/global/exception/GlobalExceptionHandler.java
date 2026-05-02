package org.example.cloth_shopping_mall.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.example.cloth_shopping_mall.global.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handlerGeneral(Exception e) {
        log.error("예상치 못한 예외 발생", e); // 스택 트레이스 로깅

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("INTERNAL_SERVER_ERROR", "SERVER_ERROR"));
    }
}
