package shopping.product.presentation.dto.response

import shopping.product.domain.Product
import java.math.BigDecimal

class ProductResponse(product: Product) {
    val id: Long = product.id
    val sellingPrice: BigDecimal = product.sellingPrice
    val fixedPrice: BigDecimal = product.fixedPrice
    val productName: String = product.productName
    val productAmount: Int = product.productAmount
    val productImage: String = product.productImage
    val productDescription: String = product.productDescription
}
