package shopping.product.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryProductRepository implements ProductRepository {
    private final Map<Long, Product> productMap = new HashMap<>();
    private final AtomicLong idSequence = new AtomicLong(1L);

    @Override
    public Product save(Product product) {
        long id = generateIdIfAbsent(product);
        product.assignId(id);
        productMap.put(id, product);
        return product;
    }

    private long generateIdIfAbsent(Product product) {
        if (product.getId() == null) {
            return idSequence.getAndIncrement();
        }
        return product.getId();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(productMap.get(id));
    }

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
    public Page<Product> findAll(Pageable pageable) {
        List<Product> all = new ArrayList<>(productMap.values());
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), all.size());
        List<Product> content = all.subList(start, end);
        return new PageImpl<>(content, pageable, all.size());
    }
}
