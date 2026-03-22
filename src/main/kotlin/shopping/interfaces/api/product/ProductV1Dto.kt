package shopping.interfaces.api.product

import shopping.domain.product.Product
import shopping.interfaces.api.Paging
import java.math.BigDecimal
import java.time.ZonedDateTime

class ProductV1Dto {
    data class CreateProductRequest(
        val name: String,
        val price: BigDecimal,
        val imageUrl: String,
    )

    data class UpdateProductRequest(
        val name: String,
        val price: BigDecimal,
        val imageUrl: String,
    )

    data class ViewResponse(
        val id: Long,
        val name: String,
        val price: BigDecimal,
        val imageUrl: String,
        val createdAt: ZonedDateTime,
    ) {
        companion object {
            fun from(product: Product): ViewResponse =
                ViewResponse(
                    id = product.id,
                    name = product.name,
                    price = product.price,
                    imageUrl = product.imageUrl,
                    createdAt = product.createdAt,
                )
        }
    }

    data class ProductSearchRequest(
        val paging: Paging.PagingRequest,
    )
}
