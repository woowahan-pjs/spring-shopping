package shopping.infrastructure.product

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import shopping.domain.product.Product
import shopping.domain.product.ProductRepository

@Component
class ProductRepositoryImpl(
    private val productJpaRepository: ProductJpaRepository,
) : ProductRepository {
    override fun findAll(pageable: Pageable): Page<Product> = productJpaRepository.findAll(pageable)

    override fun findById(id: Long): Product? = productJpaRepository.findById(id).orElse(null)

    override fun save(product: Product): Product = productJpaRepository.save(product)

    override fun deleteById(id: Long) {
        productJpaRepository.deleteById(id)
    }

    override fun existsById(id: Long): Boolean = productJpaRepository.existsById(id)
}
