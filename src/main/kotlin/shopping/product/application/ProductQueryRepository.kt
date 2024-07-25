package shopping.product.application

import shopping.product.domain.Product

interface ProductQueryRepository {
    fun findByIdAndNotDeleted(id: Long): Product?
    fun findAllAndNotDeleted(): List<Product>
}
