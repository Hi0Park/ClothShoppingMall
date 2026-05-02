package org.example.cloth_shopping_mall.controller;

import lombok.RequiredArgsConstructor;
import org.example.cloth_shopping_mall.dto.ProductDto;
import org.example.cloth_shopping_mall.global.common.ApiResponse;
import org.example.cloth_shopping_mall.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping("/each")
    public ResponseEntity<ApiResponse<ProductDto.ProductsResponse>> getEachCategoryWithLowestPrice() {
        ProductDto.ProductsResponse response = customerService.findEachCategoryWithLowestPrice();

        return ResponseEntity.ok()
                .body(ApiResponse.success(response));
    }
}
