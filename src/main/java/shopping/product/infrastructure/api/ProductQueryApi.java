package shopping.product.infrastructure.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import shopping.product.infrastructure.api.dto.ProductDetailResponse;
import shopping.product.infrastructure.api.dto.ProductReadResponse;
import shopping.product.infrastructure.persistence.ProductDao;

@RestController
public class ProductQueryApi {

    private final ProductDao productDao;

    public ProductQueryApi(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @GetMapping("/internal-api/shops/{shopId}/products/{productId}")
    public ResponseEntity<ProductReadResponse> readById(
            @PathVariable("shopId") final long shopId,
            @PathVariable("productId") final long productId
    ) {
        final ProductDetailResponse productDetailResponse = productDao.find(shopId, productId);
        return ResponseEntity.ok().body(new ProductReadResponse(
                productDetailResponse.productName(),
                productDetailResponse.amount(),
                productDetailResponse.imageUrl(),
                productDetailResponse.categoryName(),
                productDetailResponse.shopName(),
                productDetailResponse.sellerName()
        ));
    }
}
