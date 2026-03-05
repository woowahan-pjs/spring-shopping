package shopping.product.api;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shopping.auth.web.annotation.CurrentMemberId;
import shopping.auth.web.annotation.SellerOnly;
import shopping.product.service.ProductService;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    @SellerOnly
    public ResponseEntity<ProductResponse> create(
            @CurrentMemberId Long memberId,
            @Valid @RequestBody ProductCreateRequest request
    ) {
        ProductResponse response = productService.create(memberId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> get(@PathVariable Long productId) {
        ProductResponse response = productService.get(productId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{productId}")
    @SellerOnly
    public ResponseEntity<ProductResponse> update(
            @CurrentMemberId Long memberId,
            @PathVariable Long productId,
            @Valid @RequestBody ProductUpdateRequest request
    ) {
        ProductResponse response = productService.update(memberId, productId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{productId}")
    @SellerOnly
    public ResponseEntity<Void> delete(
            @CurrentMemberId Long memberId,
            @PathVariable Long productId
    ) {
        productService.delete(memberId, productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> list() {
        List<ProductResponse> response = productService.list();
        return ResponseEntity.ok(response);
    }
}
