package shopping.product.domain;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryProductRepository implements ProductRepository {
    private final Map<Long, Product> productMap = new HashMap<>();
    private final AtomicLong idSequence = new AtomicLong(1L);

    @Override
    public Product save(Product product) {
        long id = idSequence.getAndIncrement();
        product.assignId(id);
        productMap.put(id, product);
        return product;
    }

    @Override
    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(productMap.get(id));
    }

    @Override
    public Product update(Long id, Product product) {
        product.assignId(id);
        productMap.put(id, product);
        return productMap.get(id);
    }

    @Override
    public void deleteById(Long id) {
        productMap.remove(id);
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(productMap.values());
    }
}
