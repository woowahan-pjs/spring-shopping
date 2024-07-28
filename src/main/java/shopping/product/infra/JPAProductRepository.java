package shopping.product.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import shopping.product.domain.Product;
import shopping.product.domain.ProductRepository;

public interface JPAProductRepository extends ProductRepository, JpaRepository<Product, Long> {

}
