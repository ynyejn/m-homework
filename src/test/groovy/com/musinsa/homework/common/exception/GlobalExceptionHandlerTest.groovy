package com.musinsa.homework.common.exception

import com.musinsa.homework.common.exception.ApiException
import com.musinsa.homework.common.exception.GlobalExceptionHandler
import com.musinsa.homework.controller.api.response.ApiResponseWrapper
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import com.musinsa.homework.common.exception.ApiErrorCode
import spock.lang.Specification

class GlobalExceptionHandlerTest extends Specification {
    def "RuntimeException 발생 시 500 Internal Server Error 응답이 반환된다"() {
        given:
        def globalExceptionHandler = new GlobalExceptionHandler()
        def runtimeException = new RuntimeException()

        when:
        ResponseEntity<ApiResponseWrapper<Void>> response = globalExceptionHandler.handleRuntimeException(runtimeException)

        then:
        response.statusCode == HttpStatus.INTERNAL_SERVER_ERROR
        response.body.message == ApiErrorCode.INTERNAL_SERVER_ERROR.getMessage()
    }

    def "IllegalArgumentException 발생 시 400 Bad Request 응답이 반환된다"() {
        given:
        def globalExceptionHandler = new GlobalExceptionHandler()
        def illegalArgumentException = new IllegalArgumentException("Invalid parameter")

        when:
        ResponseEntity<ApiResponseWrapper<Void>> response = globalExceptionHandler.handleIllegalArgumentException(illegalArgumentException)

        then:
        response.statusCode == HttpStatus.BAD_REQUEST
        response.body.message == ApiErrorCode.INVALID_PARAMETER.getMessage()
    }
    def "ApiException 발생 시 예상한 HTTP 상태 코드와 메시지가 반환된다"() {
        given:
        def globalExceptionHandler = new GlobalExceptionHandler()
        def apiException = new ApiException(ApiErrorCode.CATEGORY_NOT_FOUND)

        when:
        ResponseEntity<ApiResponseWrapper<Void>> response = globalExceptionHandler.handleApiCustomException(apiException)

        then:
        response.statusCode == HttpStatus.NOT_FOUND
        response.body.message == ApiErrorCode.CATEGORY_NOT_FOUND.getMessage()
    }
}
