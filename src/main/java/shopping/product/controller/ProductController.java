package shopping.product.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shopping.common.PageResponse;
import shopping.product.domain.Product;
import shopping.product.controller.dto.ProductRequest;
import shopping.product.controller.dto.ProductResponse;
import shopping.product.service.ProductService;

@RestController
public class ProductController {
    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping("/products")
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest request) {
        Product save = service.save(request.toProduct());
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductResponse.from(save));
    }

    @GetMapping("/products")
    public ResponseEntity<PageResponse<ProductResponse>> getProducts(@PageableDefault(size = 20) Pageable pageable) {
        Page<ProductResponse> page = service.findProducts(pageable).map(ProductResponse::from);
        return ResponseEntity.ok(PageResponse.of(page));
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable("id") Long id) {
        Product product = service.findProductById(id);
        return ResponseEntity.ok(ProductResponse.from(product));
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable("id") Long id,
                                                      @RequestBody ProductRequest request) {
        Product product = service.update(id, request.toProduct());
        return ResponseEntity.ok(ProductResponse.from(product));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
