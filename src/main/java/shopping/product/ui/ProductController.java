package shopping.product.ui;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shopping.member.dto.MemberRequest;
import shopping.member.dto.MemberResponse;
import shopping.product.application.ProductService;
import shopping.product.dto.ProductRequest;
import shopping.product.dto.ProductResponse;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/products")
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping()
    public ResponseEntity<ProductResponse.ProductDetail> createMember(@RequestBody @Valid ProductRequest.RegProduct request) {
        ProductResponse.ProductDetail product = productService.createProduct(request);
        return ResponseEntity.created(URI.create("/products/" + product.getPrdctSn())).body(product);
    }

    @GetMapping()
    public ResponseEntity<ProductResponse.ProductsRes> findAllProducts() {
        ProductResponse.ProductsRes products = productService.findAllProducts();
        return ResponseEntity.ok().body(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse.ProductDetail> findProductBySn(@PathVariable Long id) {
        return ResponseEntity.ok().body(productService.findProductDetailResponseBySn(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse.ProductDetail> updateProduct(@PathVariable Long id, @RequestBody @Valid ProductRequest.ModProduct request) {
        ProductResponse.ProductDetail product = productService.updateProductById(id, request);
        return ResponseEntity.ok().body(product);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteProduct(@PathVariable Long id) {
        productService.deleteProductById(id);
        return ResponseEntity.noContent().build();
    }

}
