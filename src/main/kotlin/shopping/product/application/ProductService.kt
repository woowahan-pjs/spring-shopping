package shopping.product.application

import org.springframework.stereotype.Service
import shopping.product.domain.BadWordValidator
import shopping.product.domain.ProductNameContainBadWordException
import shopping.product.domain.ProductRepository

@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val badWordValidator: BadWordValidator,
) {
    fun createProduct(request: CreateProductRequest): CreateProductResponse {
        if (badWordValidator.isBadWord(request.name)) {
            throw ProductNameContainBadWordException(request.name)
        }

        val product = productRepository.save(request.toProduct())

        return CreateProductResponse(product)
    }
}
