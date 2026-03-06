package shopping.product.api.query;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shopping.product.api.query.dto.ProductDetailResponse;
import shopping.product.service.ProductQueryService;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductQueryController {
    private final ProductQueryService productQueryService;

    @GetMapping("/{productId}")
    public ProductDetailResponse findById(@PathVariable Long productId) {
        return ProductDetailResponse.from(productQueryService.findById(productId));
    }

    @GetMapping
    public List<ProductDetailResponse> findAll() {
        return productQueryService.findAll().stream()
                                  .map(ProductDetailResponse::from)
                                  .toList();
    }
}
