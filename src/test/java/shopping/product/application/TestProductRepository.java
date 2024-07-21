package shopping.product.application;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shopping.product.domain.Product;

public class TestProductRepository implements ProductRepository {
    private final Map<Long, Product> database = new HashMap<>();
    private long autoIncrement;

    @Override
    public void save(Product product) {
        database.put(++autoIncrement, product);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(database.get(id));
    }

    @Override
    public Page<Product> findAllBy(Pageable pageable) {
        return Page.empty();
    }

    @Override
    public void delete(Product product) {
        database.remove(product.getId());
    }
}
