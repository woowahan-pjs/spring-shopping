package shopping.product.infra

import org.springframework.stereotype.Repository
import shopping.product.application.ProductQueryRepository
import shopping.product.domain.Product

@Repository
class ProductQueryJdslRepository(private val productJpaRepository: ProductJpaRepository) : ProductQueryRepository {
    override fun findByIdAndNotDeleted(id: Long): Product? =
        productJpaRepository.findAll {
            val product = entity(Product::class)

            select(
                product
            ).from(
                product
            ).where(
                path(Product::deletedAt).isNull()
                    .and(
                        path(Product::id).equal(id)
                    )
            )
        }.firstOrNull()

    override fun findAllAndNotDeleted(): List<Product> =
        productJpaRepository.findAll {
            val product = entity(Product::class)

            select(
                product
            ).from(
                product
            ).where(
                path(Product::deletedAt).isNull()
            )
        }.filterNotNull()
}
