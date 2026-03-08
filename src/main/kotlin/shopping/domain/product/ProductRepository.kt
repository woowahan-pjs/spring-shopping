package shopping.domain.product

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ProductRepository {
    fun findAll(pageable: Pageable): Page<Product>

    fun findById(id: Long): Product?

    fun save(product: Product): Product

    fun deleteById(id: Long)

    fun existsById(id: Long): Boolean
}