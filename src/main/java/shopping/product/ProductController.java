package shopping.product;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final CreateProduct createProduct;
    private final FindProduct findProduct;
    private final UpdateProduct updateProduct;
    private final DeleteProduct deleteProduct;

    public ProductController(CreateProduct createProduct, FindProduct findProduct,
            UpdateProduct updateProduct, DeleteProduct deleteProduct) {
        this.createProduct = createProduct;
        this.findProduct = findProduct;
        this.updateProduct = updateProduct;
        this.deleteProduct = deleteProduct;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(@RequestBody ProductRequest request) {
        Product product = createProduct.execute(request.toProduct());
        return ResponseEntity.created(URI.create("/api/products/" + product.getId()))
                .body(ProductResponse.from(product));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findById(@PathVariable Long id) {
        Product product = findProduct.execute(id);
        return ResponseEntity.ok(ProductResponse.from(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(@PathVariable Long id,
            @RequestBody ProductRequest request) {
        Product product = updateProduct.execute(id, request.toProduct());
        return ResponseEntity.ok(ProductResponse.from(product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deleteProduct.execute(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAll() {
        List<ProductResponse> products =
                findProduct.execute().stream().map(ProductResponse::from).toList();
        return ResponseEntity.ok(products);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Void> handleNotFound(NoSuchElementException e) {
        return ResponseEntity.notFound().build();
    }
}
