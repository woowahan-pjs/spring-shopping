package shopping.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

@Repository
public class InMemoryProductRepository implements ProductRepository {

    private final Map<Long, Product> products = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    @Override
    public Product save(Product product) {
        if (product.getId() == null) {
            long id = sequence.incrementAndGet();
            Product saved =
                    new Product(id, product.getName(), product.getPrice(), product.getImageUrl());
            products.put(id, saved);
            return saved;
        }
        products.put(product.getId(), product);
        return product;
    }

    @Override
    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(products.get(id));
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(products.values());
    }

    @Override
    public void deleteById(Long id) {
        products.remove(id);
    }
}
