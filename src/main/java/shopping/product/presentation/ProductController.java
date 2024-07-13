package shopping.product.presentation;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shopping.product.application.ProductService;
import shopping.product.domain.ProductCreate;

@RequestMapping("/products")
@RestController
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody ProductCreate productCreate) {
        productService.create(productCreate);
        return ResponseEntity.ok().build();
    }
}
