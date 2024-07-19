package shopping.product.ui;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
