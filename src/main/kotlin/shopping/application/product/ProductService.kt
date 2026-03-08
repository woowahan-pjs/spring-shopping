package shopping.application.product

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import shopping.domain.product.Product
import shopping.domain.product.ProductRepository
import shopping.support.error.CoreException
import shopping.support.error.ErrorType
import java.math.BigDecimal

class ProductService(
    private val productRepository: ProductRepository
) {
    fun findByPaging(page: Int, size: Int): Page<Product> {
        return productRepository.findAll(PageRequest.of(page, size))
    }

    fun findById(id: Long): Product {
        return productRepository.findById(id)
            ?: throw CoreException(ErrorType.NOT_FOUND, "상품이 존재하지 않습니다.")
    }

    fun create(name: String, price: BigDecimal, imageUrl: String): Product {
        val product = Product(
            name = name,
            price = price,
            imageUrl = imageUrl
        )
        return productRepository.save(product)
    }

    fun update(id: Long, name: String, price: BigDecimal, imageUrl: String): Product {
        val product = findById(id)
        product.update(name, price, imageUrl)
        return productRepository.save(product)
    }

    fun delete(id: Long) {
        val product = findById(id)
        productRepository.deleteById(product.id)
    }
}
