package shopping.domain

interface ProductRepository {
    fun save(product: Product): Product

    fun findById(id: Long): Product?

    fun findAll(): List<Product>

    fun deleteById(id: Long)
}
