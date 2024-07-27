package shopping.product.domain

import jakarta.persistence.*
import shopping.global.common.BaseEntity
import java.math.BigDecimal

@Entity
@Table(
    name = "product",
)
class Product(
    sellingPrice: BigDecimal,
    fixedPrice: BigDecimal,
    productName: String,
    productAmount: Int,
    productImage: String,
    productDescription: String,
) : BaseEntity() {
    @field:Column(name = "selling_price", precision = 10, scale = 2, nullable = false)
    var sellingPrice: BigDecimal = sellingPrice
        protected set

    @field:Column(name = "fixed_price", precision = 10, scale = 2, nullable = false)
    var fixedPrice: BigDecimal = fixedPrice
        protected set

    @field:Column(name = "product_name", length = 15, nullable = false)
    var productName: String = productName
        protected set

    @field:Column(name = "product_amout", nullable = false)
    var productAmount: Int = productAmount
        protected set

    @field:Column(name = "product_image", nullable = false)
    var productImage: String = productImage
        protected set

    @field:Column(name = "product_description")
    @field:Lob
    var productDescription: String = productDescription
        protected set

    fun modify(toEntity: Product) {
        this.sellingPrice = toEntity.sellingPrice
        this.fixedPrice = toEntity.fixedPrice
        this.productName = toEntity.productName
        this.productAmount = toEntity.productAmount
        this.productImage = toEntity.productImage
        this.productDescription = toEntity.productDescription
    }
}
