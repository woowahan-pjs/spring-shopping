package shopping.product.controller;

import jakarta.validation.constraints.Min;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import shopping.product.dto.ProductResponse;
import shopping.product.service.ProductService;

@SecurityRequirement(name = "JWT")
@Validated
@Tag(name = "[상품] 상품 관리 API", description = "상품에 대한 관리를 담당합니다.")
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
class ProductController {

    private final ProductService productService;

    @PreAuthorize("hasAnyRole('CUSTOMER','ADMIN')")
    @GetMapping("/{productId}")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "상품 조회 성공"),
                @ApiResponse(responseCode = "400", description = "입력 값이 잘못 되었을 경우"),
                @ApiResponse(responseCode = "401", description = "인증 정보가 없을 때"),
                @ApiResponse(responseCode = "403", description = "유효하지 않은 권한의 회원이 요청했을 때"),
                @ApiResponse(responseCode = "500", description = "서버 오류")
            })
    @Operation(summary = "상품 조회", description = "유효한 상품을 조회 합니다.")
    public ProductResponse getProduct(
            @PathVariable @Min(value = 1, message = "상품 고유 ID는 0보다 큰 값이여야 합니다.") Long productId) {
        return productService.getProduct(productId);
    }
}
