package shopping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shopping.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
