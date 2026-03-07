package shopping.infrastructure.product

import org.springframework.data.jpa.repository.JpaRepository
import shopping.domain.product.Product

interface ProductJpaRepository : JpaRepository<Product, Long> {
}