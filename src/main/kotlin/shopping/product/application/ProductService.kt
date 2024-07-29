package shopping.product.application

import org.springframework.data.repository.findByIdOrNull
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
        checkProductNameIsNotBadWord(request.name)

        val product = productRepository.save(request.toProduct())

        return CreateProductResponse(product)
    }

    fun updateProduct(
        productId: Long,
        request: UpdateProductRequest,
    ): UpdateProductResponse {
        val product = productRepository.findByIdOrNull(productId) ?: throw ProductNotFoundException(productId)

        checkProductNameIsNotBadWord(request.name)

        product.update(
            name = request.name,
            price = request.price,
            imageUrl = request.imageUrl,
        )
        productRepository.save(product)

        return UpdateProductResponse(product)
    }

    fun getProduct(productId: Long): ProductDetailResponse {
        val product = productRepository.findByIdOrNull(productId) ?: throw ProductNotFoundException(productId)

        return ProductDetailResponse(product)
    }

    fun deleteProduct(productId: Long) {
        val product = productRepository.findByIdOrNull(productId) ?: throw ProductNotFoundException(productId)
        productRepository.delete(product)
    }

    private fun checkProductNameIsNotBadWord(name: String) {
        if (badWordValidator.isBadWord(name)) {
            throw ProductNameContainBadWordException(name)
        }
    }
}
