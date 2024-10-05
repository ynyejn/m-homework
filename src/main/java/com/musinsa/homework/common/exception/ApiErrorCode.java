package com.musinsa.homework.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiErrorCode {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "서버 내부 오류가 발생했습니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "BAD_REQUEST", "잘못된 요청입니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "NOT_FOUND", "요청한 자원을 찾을 수 없습니다."),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "INVALID_PARAMETER", "잘못된 요청입니다."),
    DATA_NOT_FOUND(HttpStatus.NOT_FOUND, "DATA_NOT_FOUND", "데이터가 존재하지 않습니다."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "CATEGORY_NOT_FOUND", "해당 카테고리를 찾을 수 없습니다."),
    BRAND_NOT_FOUND(HttpStatus.NOT_FOUND, "BRAND_NOT_FOUND", "해당 브랜드를 찾을 수 없습니다."),
    CATEGORY_BRAND_NOT_FOUND(HttpStatus.NOT_FOUND, "CATEGORY_BRAND_NOT_FOUND", "해당 카테고리 또는 브랜드를 찾을 수 없습니다."),
    ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "ITEM_NOT_FOUND", "해당 아이템을 찾을 수 없습니다."),
    NOT_ALLOW_TYPE(HttpStatus.BAD_REQUEST, "NOT_ALLOW_TYPE", "허용되지 않은 타입입니다."),
    NOT_ALLOW_ACTION(HttpStatus.BAD_REQUEST, "NOT_ALLOW_ACTION", "허용되지 않은 액션입니다."),
    UNKNOWN(HttpStatus.INTERNAL_SERVER_ERROR, "UNKNOWN", "알 수 없는 에러입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ApiErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }



}
