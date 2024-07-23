package shopping.product.infrastructure.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import shopping.product.infrastructure.api.dto.ProductReadResponse;
import shopping.product.infrastructure.api.dto.ProductDetailInfo;
import shopping.product.infrastructure.ProductReader;

@RestController
public class ProductQueryApi {

    private final ProductReader productReader;

    public ProductQueryApi(final ProductReader productReader) {
        this.productReader = productReader;
    }

    @GetMapping("/internal-api/shops/{shopId}/products/{productId}")
    public ResponseEntity<?> readById(
            @PathVariable("shopId") final long shopId,
            @PathVariable("productId") final long productId
    ) {
        final ProductDetailInfo productDetailInfo = productReader.find(shopId, productId);
        return ResponseEntity.ok().body(new ProductReadResponse(
                productDetailInfo.productName(),
                productDetailInfo.amount(),
                productDetailInfo.imageUrl(),
                productDetailInfo.categoryName(),
                productDetailInfo.shopName(),
                productDetailInfo.sellerName()
        ));
    }
}
