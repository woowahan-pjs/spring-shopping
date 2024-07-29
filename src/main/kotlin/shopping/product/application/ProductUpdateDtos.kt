package shopping.product.application

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import org.hibernate.validator.constraints.URL
import shopping.product.domain.MAX_PRODUCT_NAME_LENGTH
import shopping.product.domain.PRODUCT_NAME_PATTERN
import shopping.product.domain.Product

data class UpdateProductRequest(
    @field:Size(min = 1, max = MAX_PRODUCT_NAME_LENGTH, message = "상품명은 1자 이상 ${MAX_PRODUCT_NAME_LENGTH}자 이하여야 합니다.")
    @field:Pattern(regexp = PRODUCT_NAME_PATTERN, message = "허용되지 않은 특수문자가 포함되어있습니다.")
    val name: String,
    @field:Min(0)
    val price: Int,
    @field:URL
    val imageUrl: String,
    @field:Min(0)
    val stockQuantity: Int,
)

data class UpdateProductResponse(
    val productId: Long,
    val name: String,
    val price: Int,
    val imageUrl: String,
) {
    constructor(product: Product) : this(
        productId = product.id,
        name = product.name,
        price = product.price,
        imageUrl = product.imageUrl,
    )
}
