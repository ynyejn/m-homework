package com.musinsa.homework.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "API 응답 래퍼")
public class ApiResponseWrapper<T> {
    @Schema(description = "성공 여부", example = "true")
    private final boolean success;
    @Schema(description = "응답 데이터")
    private final T data;
    @Schema(description = "메세지")
    private final String message;

    private ApiResponseWrapper(boolean success, T data, String message) {
        this.success = success;
        this.data = data;
        this.message = message;
    }

    public static <T> ApiResponseWrapper<T> success(T data) {
        return new ApiResponseWrapper<>(true, data, null);
    }

    public static <T> ApiResponseWrapper<T> error(String message) {
        return new ApiResponseWrapper<>(false, null, message);
    }

    @Schema(name = "ApiResponseWrapperWithError", description = "에러 발생시 API 응답 래퍼")
    public static class WithError extends ApiResponseWrapper<Void> {
        @Schema(description = "성공 여부", example = "false")
        private final boolean success = false;

        private WithError( String message) {
            super(false, null, message);
        }
    }

}
