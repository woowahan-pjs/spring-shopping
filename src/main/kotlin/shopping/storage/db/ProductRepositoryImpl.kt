package shopping.storage.db

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import shopping.domain.Product
import shopping.domain.ProductRepository
import shopping.support.error.CoreException
import shopping.support.error.ErrorType

@Repository
class ProductRepositoryImpl(
    private val productJpaRepository: ProductJpaRepository,
) : ProductRepository {
    override fun save(product: Product): Product {
        if (product.id != 0L) {
            val entity =
                productJpaRepository.findByIdOrNull(product.id)
                    ?: throw CoreException(ErrorType.PRODUCT_NOT_FOUND)
            entity.update(product)
            return entity.toDomain()
        }
        val entity = ProductEntity.from(product)
        return productJpaRepository.save(entity).toDomain()
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
