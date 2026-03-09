package shopping.product;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataProductRepository
        extends JpaRepository<Product, UUID>, ProductRepository {
}
