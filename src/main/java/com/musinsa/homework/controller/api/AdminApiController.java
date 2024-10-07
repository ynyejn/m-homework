package com.musinsa.homework.controller.api;


import com.musinsa.homework.controller.api.request.BrandItemManagementRequestDto;
import com.musinsa.homework.controller.api.response.ApiResponseWrapper;
import com.musinsa.homework.controller.api.response.ResultResponseDto;
import com.musinsa.homework.service.AdminApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/admin")
@Tag(name = "admin", description = "Admin API")
@RequiredArgsConstructor
public class AdminApiController {

    private final AdminApplicationService adminApplicationService;

    @PostMapping("/brand-item-management")
    @Operation(summary = "브랜드 및 상품 관리",
            description = "4.운영자가 새로운 브랜드를 등록하고, 모든 브랜드의 상품을 추가, 변경, 삭제할 수 있습니다.")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ApiResponseWrapper.class)))
    @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = ApiResponseWrapper.WithError.class)))
    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(implementation = ApiResponseWrapper.WithError.class)))
    public ResponseEntity<ApiResponseWrapper<ResultResponseDto>> manageBrandAndItems(
            @RequestBody @Valid BrandItemManagementRequestDto request) {
        ResultResponseDto response = adminApplicationService.manageBrandAndItems(request);
        return ResponseEntity.ok(ApiResponseWrapper.success(response));
    }
}