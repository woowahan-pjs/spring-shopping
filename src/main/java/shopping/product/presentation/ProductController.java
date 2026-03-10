package shopping.product.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shopping.common.auth.AuthRole;
import shopping.common.auth.RoleRequired;
import shopping.common.dto.ApiResponse;
import shopping.product.application.dto.ProductCreateRequest;
import shopping.product.application.dto.ProductCreateResponse;
import shopping.product.application.dto.ProductGetResponse;
import shopping.product.application.dto.ProductUpdateRequest;
import shopping.product.application.command.*;

import java.util.List;
import shopping.product.application.dto.ProductUpdateResponse;
import shopping.product.application.query.ProductQueryService;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductCreateUseCase productCreateUseCase;
    private final ProductQueryService productQueryService;
    private final ProductUpdateUseCase productUpdateUseCase;
    private final ProductDeleteUseCase productDeleteUseCase;

    @RoleRequired(AuthRole.ADMIN)
    @PostMapping
    public ResponseEntity<ApiResponse<ProductCreateResponse>> create(
        @Valid @RequestBody ProductCreateRequest request) {
        ProductCreateResponse response = productCreateUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.of(HttpStatus.CREATED, response));
    }

    @RoleRequired(AuthRole.ADMIN)
    @PatchMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductUpdateResponse>> update(
        @PathVariable Long productId,
        @Valid @RequestBody ProductUpdateRequest request) {
        ProductUpdateResponse response = productUpdateUseCase.execute(productId, request);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @RoleRequired(AuthRole.ADMIN)
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse<Long>> delete(@PathVariable Long productId) {
        Long response = productDeleteUseCase.execute(productId);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductGetResponse>> getById(
        @PathVariable Long productId) {
        ProductGetResponse response = productQueryService.getById(productId);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductGetResponse>>> getAll() {
        List<ProductGetResponse> response = productQueryService.getAll();
        return ResponseEntity.ok(ApiResponse.ok(response));
    }
}
