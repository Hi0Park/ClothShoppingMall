package org.example.cloth_shopping_mall.controller;

import lombok.RequiredArgsConstructor;
import org.example.cloth_shopping_mall.dto.ProductDto;
import org.example.cloth_shopping_mall.global.common.ApiResponse;
import org.example.cloth_shopping_mall.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping("/lowest/each")
    public ResponseEntity<ApiResponse<ProductDto.ProductsForCategoryResponse>> getEachCategoryWithLowestPrice() {
        ProductDto.ProductsForCategoryResponse response = customerService.findLowestPriceWithEachCategory();

        return ResponseEntity.ok()
                .body(ApiResponse.success(response));
    }

    @GetMapping("/lowest/same-brand")
    public ResponseEntity<ApiResponse<ProductDto.ProductsForOneBrandResponse>> getLowestProductWithOneBrand() {
        ProductDto.ProductsForOneBrandResponse response = customerService.findLowestPriceWithOneBrand();

        return ResponseEntity.ok()
                .body(ApiResponse.success(response));
    }
}
