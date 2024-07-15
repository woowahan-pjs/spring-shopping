package shopping.product.application

import shopping.product.domain.Product
import java.time.LocalDateTime

data class ProductDetailResponse(
    val id: Long,
    val name: String,
    val price: Int,
    val imageUrl: String,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
) {
    constructor(product: Product) : this(
        id = product.id,
        name = product.name,
        price = product.price,
        imageUrl = product.imageUrl,
        createdAt = product.createdAt,
        updatedAt = product.updatedAt,
    )
}
