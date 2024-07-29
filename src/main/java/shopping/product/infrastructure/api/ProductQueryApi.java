package shopping.product.infrastructure.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shopping.product.infrastructure.api.dto.ProductDetailResponse;
import shopping.product.infrastructure.api.dto.ProductInfo;
import shopping.product.infrastructure.api.dto.ProductInfosHttpResponse;
import shopping.product.infrastructure.persistence.ProductDao;

import java.util.List;

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
        final ProductDetailResponse productDetailResponse = productDao.findProductDetailByProductId(productId);
        return ResponseEntity.ok().body(productDetailResponse);
    }

    @GetMapping("/api/products/categories/{categoryId}")
    public ResponseEntity<ProductInfosHttpResponse> readByCategory(
            @PathVariable("categoryId") final long categoryId,
            @RequestParam(value = "startId", required = false, defaultValue = "1") final long startId,
            @RequestParam(value = "limit", required = false, defaultValue = "10") final long limit
    ) {
        if (limit > 100) {
            throw new RuntimeException();
        }
        final List<ProductInfo> productInfos = productDao.findProductListByCategoryId(categoryId, startId, limit);
        return ResponseEntity.ok().body(new ProductInfosHttpResponse(productInfos));
    }

    @GetMapping("/api/products/shops/{shopId}")
    public ResponseEntity<ProductInfosHttpResponse> readByShop(
            @PathVariable("shopId") final long shopId,
            @RequestParam(value = "startId", required = false, defaultValue = "1") final long startId,
            @RequestParam(value = "limit", required = false, defaultValue = "10") final long limit
    ) {
        if (limit > 100) {
            throw new RuntimeException();
        }
        final List<ProductInfo> productInfos = productDao.findProductListByShopId(shopId, startId, limit);
        return ResponseEntity.ok().body(new ProductInfosHttpResponse(productInfos));
    }
}
