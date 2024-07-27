package shopping.product.application

import shopping.product.domain.Product

interface ProductCommandRepository {
    fun save(product: Product): Product
}
