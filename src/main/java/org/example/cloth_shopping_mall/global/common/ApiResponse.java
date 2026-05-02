package org.example.cloth_shopping_mall.global.common;

public record ApiResponse<T>(
        boolean success,
        T data,
        ErrorInfo error
) {
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null);
    }

    public static <T> ApiResponse<T> error(String message, String code) {
        return new ApiResponse<>(false, null, new ErrorInfo(message, code));
    }

    public record ErrorInfo(String message, String code) {}
}
