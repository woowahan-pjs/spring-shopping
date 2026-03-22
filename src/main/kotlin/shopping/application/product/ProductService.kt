package shopping.application.product

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import shopping.domain.product.Product
import shopping.domain.product.ProductBadWordClient
import shopping.domain.product.ProductRepository
import shopping.support.error.CoreException
import shopping.support.error.ErrorType
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val productBadWordClient: ProductBadWordClient
) {
    fun findByPaging(page: Int, size: Int): Page<Product> {
        return productRepository.findAll(PageRequest.of(page, size))
    }

    fun findById(id: Long): Product {
        return productRepository.findById(id)
            ?: throw CoreException(ErrorType.NOT_FOUND, "상품이 존재하지 않습니다.")
    }

    fun create(name: String, price: BigDecimal, imageUrl: String): Product {
        validateProfanity(name)
        val product = Product(
            name = name,
            price = price,
            imageUrl = imageUrl
        )
        return productRepository.save(product)
    }

    fun update(id: Long, name: String, price: BigDecimal, imageUrl: String): Product {
        validateProfanity(name)
        val product = findById(id)
        product.update(name, price, imageUrl)
        return productRepository.save(product)
    }

    private fun validateProfanity(name: String) {
        if (productBadWordClient.containsProfanity(name)) {
            throw CoreException(ErrorType.BAD_REQUEST, "상품 이름에 비속어를 포함할 수 없습니다.")
        }
    }

    fun delete(id: Long) {
        val product = findById(id)
        productRepository.deleteById(product.id)
    }
}
