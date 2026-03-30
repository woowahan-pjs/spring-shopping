package shopping.product;

import shopping.product.domain.Product;
import shopping.product.domain.ProductRepository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataProductRepository
        extends JpaRepository<Product, UUID>, ProductRepository {
}
