package shopping.utils.fake;

import shopping.product.domain.Product;
import shopping.product.domain.repository.ProductRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class FakeProductRepository implements ProductRepository {
    private final Map<Long, Product> storage = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    @Override
    public Product save(final Product product) {
        if (Objects.isNull(product.id())) {
            final var id = idGenerator.incrementAndGet();
            final var newProduct = new Product(
                    id,
                    product.name(),
                    product.amount(),
                    product.thumbnailImageUrl(),
                    product.subCategoryId(),
                    product.shopId(),
                    product.sellerId(),
                    product.detailedImageUrls()
            );
            storage.put(id, newProduct);
            return newProduct;
        }
        return storage.put(product.id(), product);
    }
}
