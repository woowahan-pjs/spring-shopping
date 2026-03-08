package shopping.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import shopping.domain.Product;
import shopping.dto.ProductRequest;
import shopping.dto.ProductResponse;
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
}
