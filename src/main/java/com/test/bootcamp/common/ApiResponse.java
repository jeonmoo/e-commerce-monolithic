package com.test.bootcamp.common;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
public class ApiResponse<T> {
    private final Boolean isSuccess;
    private final Error error;
    private final T result;

    @Getter
    private static class Error {
        private final String code;
        private final String message;

        @Builder
        public Error(String code, String message) {
            this.code = code;
            this.message = message;
        }
    }

    public ApiResponse(Boolean isSuccess, String errorCode, String errorMessage, T result) {
        this.isSuccess = isSuccess;
        this.error = Error.builder()
                .code(errorCode)
                .message(errorMessage)
                .build();
        this.result = result;
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(T result) {
        return ResponseEntity.ok(new ApiResponse<>(true, null, null, result));
    }

    public static <T> ResponseEntity<ApiResponse<T>> fail(String errorCode, String errorMessage) {
        return ResponseEntity.ok(new ApiResponse<>(false, errorCode, errorMessage, null));
    }

    public static <T> ResponseEntity<ApiResponse<T>> serverException(String errorCode, String errorMessage) {
        return ResponseEntity.status(200).body(new ApiResponse<>(false, errorCode, errorMessage, null));
    }

}
