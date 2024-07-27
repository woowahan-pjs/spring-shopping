package shopping.product.application

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import shopping.global.exception.ApplicationException
import shopping.global.exception.ErrorCode
import shopping.product.domain.Product

@Service
@Transactional(readOnly = true)
class ProductQueryService(private val productQueryRepository: ProductQueryRepository) {
    fun findProductNotDeleted(productId: Long): Product {
        productQueryRepository.findByIdAndNotDeleted(productId)?.let { return it }
        throw ApplicationException(ErrorCode.PRODUCT_NOT_FOUND)
    }

    fun findAllNotDeleted(): List<Product> {
        return productQueryRepository.findAllAndNotDeleted()
    }
}
