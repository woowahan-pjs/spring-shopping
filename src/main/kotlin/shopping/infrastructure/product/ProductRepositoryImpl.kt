package shopping.infrastructure.product

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import shopping.domain.product.Product
import shopping.domain.product.ProductRepository

@Component
class ProductRepositoryImpl(
    private val productJpaRepository: ProductJpaRepository
) : ProductRepository {
    override fun findAll(pageable: Pageable): Page<Product> {
        return productJpaRepository.findAll(pageable);
    }

    override fun existsById(id: Long): Boolean {
        return productJpaRepository.existsById(id)
    }
}