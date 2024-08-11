package shopping.product.ui;

import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shopping.product.application.ProductService;
import shopping.product.application.dto.ProductCreateRequest;
import shopping.product.application.dto.ProductUpdateRequest;
import shopping.product.domain.Product;

@RequestMapping("/api/products")
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping()
    public ResponseEntity<Void> create(@RequestBody @Valid final ProductCreateRequest request) {
        final Long saved = productService.create(request);
        return ResponseEntity.created(URI.create("/api/products/" + saved))
                .build();
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Void> update(@PathVariable Long productId,
            @RequestBody @Valid final ProductUpdateRequest request) {
        productService.update(productId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> delete(@PathVariable Long productId) {
        productService.delete(productId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> find(@PathVariable Long productId) {
        final Product product = productService.findProduct(productId);
        return ResponseEntity.ok().body(product);
    }
}
