package com.musinsa.homework.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;


@Data
@Schema(description = "BrandOperation 또는 ItemOperation 중 하나만 입력되어야 합니다. " +
                "BrandOperation은 브랜드 관리 작업 정보를 포함하고, " +
                "ItemOperation은 상품 관리 작업 정보를 포함합니다."
)
public class BrandItemManagementRequestDto {

    @Schema(description = "브랜드 관리 작업 정보")
    @Valid
    private BrandOperation brandOperation;

    @Schema(description = "상품 관리 작업 정보")
    @Valid
    private ItemOperation itemOperation;

    @AssertTrue(message = "BrandOperation 또는 ItemOperation 중 하나만 입력되어야 합니다.")
    @Schema(hidden = true)
    public boolean isValid() {
        return (brandOperation != null && itemOperation == null) || (brandOperation == null && itemOperation != null);
    }

    @Data
    @Schema(description = "브랜드 관리 작업")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BrandOperation {

        @Schema(description = "브랜드 작업 유형", example = "CREATE", allowableValues = {"CREATE"})
        @NotNull(message = "작업 유형은 필수입니다.")
        private ActionType action;

        @Schema(description = "브랜드 이름", example = "Nike")
        @NotNull(message = "브랜드 이름은 필수입니다.")
        private String brandName;
    }

    @Data
    @Schema(description = "상품 관리 작업")
    @RequiredArgsConstructor
    public static class ItemOperation {

        @Schema(description = "상품 작업 유형", example = "UPDATE", allowableValues = {"CREATE", "UPDATE", "DELETE"})
        @NotNull(message = "작업 유형은 필수입니다.")
        private ActionType action;

        @Schema(description = "상품 ID (UPDATE 또는 DELETE 시 필요)", example = "123")
        private Long itemId;

        @Schema(description = "브랜드 ID", example = "1")
        private Long brandId;

        @Schema(description = "카테고리 ID", example = "1")
        private Long categoryId;

        @Schema(description = "상품 가격", example = "12000")
        private Integer price;
    }

}