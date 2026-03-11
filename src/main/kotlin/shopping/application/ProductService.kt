package shopping.application

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import shopping.application.dto.CreateProductCommand
import shopping.application.dto.ProductResult
import shopping.application.dto.UpdateProductCommand
import shopping.domain.Product
import shopping.domain.ProductRepository
import shopping.domain.ProfanityFilter
import shopping.support.error.CoreException
import shopping.support.error.ErrorType

@Service
@Transactional(readOnly = true)
class ProductService(
    private val productRepository: ProductRepository,
    private val profanityFilter: ProfanityFilter,
) {
    @Transactional
    fun create(command: CreateProductCommand): Long {
        validateProfanity(command.name)

        val product =
            Product(
                name = command.name,
                price = command.price,
                imageUrl = command.imageUrl,
            )
        return productRepository.save(product).id
    }

    @Transactional
    fun update(
        id: Long,
        command: UpdateProductCommand,
    ) {
        validateProfanity(command.name)

        val product = findProductById(id)
        product.update(
            name = command.name,
            price = command.price,
            imageUrl = command.imageUrl,
        )
        productRepository.save(product)
    }

    fun getProduct(id: Long): ProductResult {
        val product = findProductById(id)
        return ProductResult.from(product)
    }

    fun getProducts(): List<ProductResult> {
        val products = productRepository.findAll()
        return products.map { ProductResult.from(it) }
    }

    @Transactional
    fun delete(id: Long) {
        val product = findProductById(id)
        productRepository.deleteById(product.id)
    }

    private fun validateProfanity(name: String) {
        if (profanityFilter.containsProfanity(name)) {
            throw CoreException(ErrorType.PRODUCT_PROFANITY_DETECTED)
        }
    }

    private fun findProductById(id: Long): Product = productRepository.findById(id) ?: throw CoreException(ErrorType.PRODUCT_NOT_FOUND)
}
