package shopping.product.ui;

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
import shopping.product.application.dto.ProductModifyRequest;
import shopping.product.application.dto.ProductResponse;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(final ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody final ProductCreateRequest request) {
        final ProductResponse productResponse = productService.save(request);
        return ResponseEntity.created(URI.create("/products/" + productResponse.getId())).body(productResponse);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> findProducts() {
        return ResponseEntity.ok().body(productService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findProduct(@PathVariable("id") final Long id) {
        return ResponseEntity.ok().body(productService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> modifyProduct(@PathVariable("id") final Long id, @RequestBody final ProductModifyRequest request) {
        productService.update(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductResponse> removeProduct(@PathVariable final Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
