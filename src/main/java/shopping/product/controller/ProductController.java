package shopping.product.controller;

import java.net.URI;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import shopping.infra.security.UserPrincipal;
import shopping.product.dto.ProductResponse;
import shopping.product.dto.ProductSaveRequest;
import shopping.product.dto.ProductSearchRequest;
import shopping.product.dto.ProductUpdateRequest;
import shopping.product.dto.ProductsSearchResponse;
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
            @PathVariable @Min(value = 1, message = "상품 고유 ID는 0보다 큰 값이여야 합니다.")
                    final Long productId) {
        return productService.getProduct(productId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "상품 등록 성공"),
                @ApiResponse(responseCode = "400", description = "입력 값이 잘못 되었거나, 비속어가 존재할 경우"),
                @ApiResponse(responseCode = "401", description = "인증 정보가 없을 때"),
                @ApiResponse(responseCode = "403", description = "유효하지 않은 권한의 회원이 요청했을 때"),
                @ApiResponse(responseCode = "500", description = "서버 오류")
            })
    @Operation(summary = "상품 저장", description = "상품을 등록합니다.")
    public ResponseEntity<Void> registerProduct(
            @AuthenticationPrincipal final UserPrincipal userPrincipal,
            @RequestBody @Valid final ProductSaveRequest request) {
        final Long productId = productService.registerProduct(userPrincipal.getId(), request);

        final URI location =
                ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{productId}")
                        .buildAndExpand(productId)
                        .toUri();

        return ResponseEntity.created(location).build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{productId}")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "상품 수정 성공"),
                @ApiResponse(responseCode = "400", description = "입력 값이 문제, 상품이 없거나 비속어가 존재할 경우"),
                @ApiResponse(responseCode = "401", description = "인증 정보가 없을 때"),
                @ApiResponse(responseCode = "403", description = "유효하지 않은 권한의 회원이 요청했을 때"),
                @ApiResponse(responseCode = "500", description = "서버 오류")
            })
    @Operation(summary = "상품 수정", description = "상품을 수정합니다.")
    public void modifyProduct(
            @PathVariable @Min(value = 1, message = "상품 고유 ID는 0보다 큰 값이여야 합니다.")
                    final Long productId,
            @AuthenticationPrincipal final UserPrincipal userPrincipal,
            @RequestBody @Valid final ProductUpdateRequest request) {
        productService.modifyProduct(userPrincipal.getId(), productId, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{productId}")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "상품 삭제 성공"),
                @ApiResponse(responseCode = "400", description = "입력 값의 문제"),
                @ApiResponse(responseCode = "401", description = "인증 정보가 없을 때"),
                @ApiResponse(responseCode = "403", description = "유효하지 않은 권한의 회원이 요청했을 때"),
                @ApiResponse(responseCode = "500", description = "서버 오류")
            })
    @Operation(summary = "상품 삭제", description = "상품을 삭제합니다.")
    public void removeProduct(
            @PathVariable @Min(value = 1, message = "상품 고유 ID는 0보다 큰 값이여야 합니다.")
                    final Long productId,
            @AuthenticationPrincipal final UserPrincipal userPrincipal) {
        productService.removeProduct(userPrincipal.getId(), productId);
    }

    @PreAuthorize("hasAnyRole('CUSTOMER','ADMIN')")
    @GetMapping
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "201", description = "상품 검색 성공"),
                @ApiResponse(responseCode = "400", description = "입력 값이 잘못 되었을 경우"),
                @ApiResponse(responseCode = "401", description = "인증 정보가 없을 때"),
                @ApiResponse(responseCode = "403", description = "유효하지 않은 권한의 회원이 요청했을 때"),
                @ApiResponse(responseCode = "500", description = "서버 오류")
            })
    @Operation(summary = "상품 검색", description = "상품을 검색 합니다.")
    public ProductsSearchResponse search(
            @Valid final ProductSearchRequest request,
            @PageableDefault @ParameterObject final Pageable pageable) {
        return productService.searchProduct(request, pageable);
    }
}
