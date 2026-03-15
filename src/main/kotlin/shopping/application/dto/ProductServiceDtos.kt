package shopping.application.dto

import shopping.domain.Product

data class CreateProductCommand(
    val name: String,
    val price: Int,
    val imageUrl: String,
)

data class UpdateProductCommand(
    val name: String,
    val price: Int,
    val imageUrl: String,
)

data class ProductResult(
    val id: Long,
    val name: String,
    val price: Int,
    val imageUrl: String,
) {
    companion object {
        fun from(product: Product): ProductResult =
            ProductResult(
                id = product.id,
                name = product.name.value,
                price = product.price.value,
                imageUrl = product.imageUrl,
            )
    }
}
