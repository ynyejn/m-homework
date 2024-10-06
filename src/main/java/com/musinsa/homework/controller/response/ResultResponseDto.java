package com.musinsa.homework.controller.response;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class ResultResponseDto {
    private Boolean result;
    private String message;

    @Builder
    public ResultResponseDto(Boolean result, String message) {
        this.result = result;
        this.message = message;
    }

    public static ResultResponseDto success(){
        return ResultResponseDto.builder().result(true).build();
    }

    public static ResultResponseDto fail(String message){
        return ResultResponseDto.builder().result(false).message(message).build();
    }
}
