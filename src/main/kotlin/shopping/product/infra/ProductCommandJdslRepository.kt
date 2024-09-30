package shopping.product.infra

import org.springframework.stereotype.Repository
import shopping.product.application.ProductCommandRepository
import shopping.product.domain.Product

@Repository
class ProductCommandJdslRepository(private val productJpaRepository: ProductJpaRepository): ProductCommandRepository {
    override fun save(product: Product): Product = productJpaRepository.save(product)
}
