package shopping.product.application.command

import shopping.product.domain.Product
import java.math.BigDecimal

data class ProductCreateCommand(
    val sellingPrice: BigDecimal,
    val fixedPrice: BigDecimal,
    val productName: String,
    val productAmount: Int,
    val productImage: String,
    val productDescription: String,
) {
    fun toEntity(): Product =
        Product(
            sellingPrice,
            fixedPrice,
            productName,
            productAmount,
            productImage,
            productDescription,
        )
}
