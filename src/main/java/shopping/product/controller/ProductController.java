package shopping.product.controller;

import shopping.product.domain.*;
import shopping.product.dto.*;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

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
    public ResponseEntity<CreateProductResponse> create(@RequestBody ProductRequest request) {
        CreateProductResponse response =
                createProduct.execute(request.name(), request.price(), request.imageUrl());
        return ResponseEntity.created(URI.create("/api/products/" + response.id())).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(findProduct.execute(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable UUID id, @RequestBody ProductRequest request) {
        updateProduct.execute(id, request.name(), request.price(), request.imageUrl());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteProduct.execute(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAll() {
        return ResponseEntity.ok(findProduct.execute());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Void> handleNotFound(NoSuchElementException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(ProfanityCheckException.class)
    public ResponseEntity<Map<String, String>> handleProfanityCheckFailure(
            ProfanityCheckException e) {
        return ResponseEntity.internalServerError().body(Map.of("message", e.getMessage()));
    }
}
