package shopping.api.presentation.v1.dto

import shopping.application.dto.CreateProductCommand
import shopping.application.dto.ProductResult
import shopping.application.dto.UpdateProductCommand

data class CreateProductRequest(
    val name: String,
    val price: Int,
    val imageUrl: String,
) {
    fun toCommand() = CreateProductCommand(name, price, imageUrl)
}

data class UpdateProductRequest(
    val name: String,
    val price: Int,
    val imageUrl: String,
) {
    fun toCommand() = UpdateProductCommand(name, price, imageUrl)
}

data class ProductResponse(
    val id: Long,
    val name: String,
    val price: Int,
    val imageUrl: String,
) {
    companion object {
        fun from(result: ProductResult) =
            ProductResponse(
                id = result.id,
                name = result.name,
                price = result.price,
                imageUrl = result.imageUrl,
            )
    }
}
