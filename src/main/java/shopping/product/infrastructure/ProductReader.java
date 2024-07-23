package shopping.product.infrastructure;

import org.springframework.stereotype.Component;
import shopping.product.infrastructure.api.dto.ProductDetailInfo;
import shopping.product.infrastructure.persistence.ProductDao;

@Component
public class ProductReader {

    private final ProductDao productDao;

    public ProductReader(final ProductDao productDao) {
        this.productDao = productDao;
    }

    public ProductDetailInfo find(final long shopId, final long productId) {
        return productDao.find(shopId, productId);
    }
}
