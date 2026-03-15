package shopping.storage.db

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import shopping.domain.Product
import shopping.domain.ProductRepository

@Repository
class ProductRepositoryImpl(
    private val productJpaRepository: ProductJpaRepository,
) : ProductRepository {
    override fun save(product: Product): Product {
        val entity = ProductEntity.from(product)
        return productJpaRepository.save(entity).toDomain()
    }

    override fun update(product: Product): Product {
        val entity =
            checkNotNull(productJpaRepository.findByIdOrNull(product.id)) {
                "Product with id ${product.id} not found"
            }
        entity.update(product)
        return entity.toDomain()
    }

    override fun findById(id: Long): Product? {
        val entity =
            productJpaRepository.findByIdOrNull(id)
                ?: return null
        return entity.toDomain()
    }

    override fun findAll(): List<Product> {
        val entities = productJpaRepository.findAll()
        return entities.map { it.toDomain() }
    }

    override fun deleteById(id: Long) {
        productJpaRepository.deleteById(id)
    }
}
