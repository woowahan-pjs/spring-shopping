package shopping.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shopping.domain.Product;
import shopping.controller.dto.ProductRequest;
import shopping.controller.dto.ProductResponse;
import shopping.service.ProductService;

import java.util.List;

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
    public ResponseEntity<List<ProductResponse>> getProducts() {
        List<Product> products = service.findProducts();
        return ResponseEntity.ok(products.stream().map(ProductResponse::from).toList());
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
