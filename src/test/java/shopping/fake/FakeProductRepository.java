package shopping.fake;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import shopping.product.domain.Product;
import shopping.product.domain.ProductRepository;

public class FakeProductRepository implements ProductRepository {

    private final Map<Long, Product> products = new HashMap<>();

    @Override
    public Product save(final Product product) {
        products.put(product.getId(), product);
        return product;
    }

    @Override
    public void delete(final Product product) {
        products.remove(product.getId());
    }

    @Override
    public Optional<Product> findById(final Long id) {
        return Optional.ofNullable(products.get(id));
    }

    @Override
    public List<Product> findByIdIn(final List<Long> ids) {
        return ids.stream()
                .map(products::get)
                .filter(Objects::nonNull)
                .toList();
    }
}
