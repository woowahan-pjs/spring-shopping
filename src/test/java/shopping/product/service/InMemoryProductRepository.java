package shopping.product.service;

import shopping.product.domain.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InMemoryProductRepository implements ProductRepository {

    private final Map<UUID, Product> store = new HashMap<>();

    @Override
    public Product save(Product product) {
        store.put(product.getId(), product);
        return product;
    }

    @Override
    public Optional<Product> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Optional<Product> findByIdAndStatus(UUID id, ProductStatus status) {
        return Optional.ofNullable(store.get(id)).filter(p -> p.getStatus() == status);
    }

    @Override
    public List<Product> findAllByStatus(ProductStatus status) {
        return store.values().stream().filter(p -> p.getStatus() == status).toList();
    }

    @Override
    public List<Product> findAllByIdInAndStatus(List<UUID> ids, ProductStatus status) {
        return ids.stream().map(store::get).filter(p -> p != null && p.getStatus() == status)
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        store.remove(id);
    }
}
