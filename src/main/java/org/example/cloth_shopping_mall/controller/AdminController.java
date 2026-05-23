package org.example.cloth_shopping_mall.controller;


import lombok.RequiredArgsConstructor;
import org.example.cloth_shopping_mall.dto.ProductDto;
import org.example.cloth_shopping_mall.global.common.ApiResponse;
import org.example.cloth_shopping_mall.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/product")
    public ResponseEntity<ApiResponse<Long>> addProduct(
            @RequestBody ProductDto.AddProductRequest request
            ) {
        Long productId = adminService.addProduct(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(productId));
    }

    // 요청 방식의 일관성 유지를 위해 JSON 요청을 받도록 함.
    @DeleteMapping("/product")
    public ResponseEntity<ApiResponse<Long>> deleteProduct(
            @RequestBody ProductDto.DeleteProductRequest request
    ) {
        adminService.deleteProduct(request.id());

        return ResponseEntity.ok()
                .body(ApiResponse.success(request.id()));
    }


    // 현재는 수정할 데이터가 Product의 Price 밖에 없으나, 만약 상품명과 같은 수정이 필요한 DTO 필드 추가를 위해서,
    // JSON으로 받도록 작성함.
    @PatchMapping("/product")
    public ResponseEntity<ApiResponse<Long>> updateProduct(
            @RequestBody ProductDto.UpdateProductRequest request
    ) {
        Long id = adminService.updateProduct(request);

        return ResponseEntity.ok()
                .body(ApiResponse.success(id));
    }

}
