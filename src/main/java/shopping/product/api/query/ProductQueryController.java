package shopping.product.api.query;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shopping.common.response.PageResponse;
import shopping.product.api.query.dto.ProductDetailResponse;
import shopping.product.service.ProductQueryService;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductQueryController {
    private final ProductQueryService productQueryService;

    @GetMapping("/{productId}")
    public ProductDetailResponse findById(@PathVariable Long productId) {
        return ProductDetailResponse.from(productQueryService.getProduct(productId));
    }

    @GetMapping
    public PageResponse<ProductDetailResponse> findAll(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return PageResponse.from(
                productQueryService.findProductWithPage(pageable)
                                   .map(ProductDetailResponse::from)
        );
    }
}
