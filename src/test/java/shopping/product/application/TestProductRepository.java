package shopping.product.application;

import java.util.HashMap;
import java.util.Map;
import shopping.product.domain.Product;

public class TestProductRepository implements ProductRepository {
    private final Map<Long, Product> database = new HashMap<>();

    @Override
    public void save(Product product) {
        database.put(product.getId(), product);
    }
}
