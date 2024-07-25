package shopping.product.infrastructure.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shopping.product.infrastructure.api.dto.ProductDetailResponse;
import shopping.product.infrastructure.api.dto.ProductListResponse;
import shopping.product.infrastructure.persistence.ProductDao;

@RestController
public class ProductQueryApi {

    private final ProductDao productDao;

    public ProductQueryApi(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @GetMapping("/api/products/{productId}")
    public ResponseEntity<ProductDetailResponse> readById(
            @PathVariable("productId") final long productId
    ) {
        final ProductDetailResponse productDetailResponse = productDao.findProductDetail(productId);
        return ResponseEntity.ok().body(productDetailResponse);
    }

    @GetMapping("/api/products/categories/{categoryId}")
    public ResponseEntity<ProductListResponse> readByCategory(
            @PathVariable("categoryId") final long categoryId,
            @RequestParam(value = "startId", required = false, defaultValue = "1") final long startId,
            @RequestParam(value = "limit", required = false, defaultValue = "10") final long limit
    ) {
        if (limit > 100) {
            throw new RuntimeException();
        }
        final ProductListResponse productListResponse = productDao.findProductListByCategoryId(categoryId, startId, limit);
        return ResponseEntity.ok().body(productListResponse);
    }

    @GetMapping("/api/products/shops/{shopId}")
    public ResponseEntity<ProductListResponse> readByShop(
            @PathVariable("shopId") final long shopId,
            @RequestParam(value = "startId", required = false, defaultValue = "1") final long startId,
            @RequestParam(value = "limit", required = false, defaultValue = "10") final long limit
    ) {
        if (limit > 100) {
            throw new RuntimeException();
        }
        final ProductListResponse productListResponse = productDao.findProductListByShopId(shopId, startId, limit);
        return ResponseEntity.ok().body(productListResponse);
    }
}
