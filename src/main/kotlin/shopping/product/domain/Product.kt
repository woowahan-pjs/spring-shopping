package shopping.product.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import shopping.common.domain.BaseEntity

const val MAX_PRODUCT_NAME_LENGTH: Int = 15
const val PRODUCT_NAME_PATTERN: String = "^[a-zA-Z0-9 ()\\[\\]+\\-&/_]*$"

@Entity
class Product(
    name: String,
    price: Int,
    imageUrl: String,
) : BaseEntity() {
    @Column(length = MAX_PRODUCT_NAME_LENGTH)
    var name: String = name
        protected set

    @Column
    var price: Int = price
        protected set

    @Column
    var imageUrl: String = imageUrl
        protected set

    fun update(
        name: String,
        price: Int,
        imageUrl: String,
    ) {
        this.name = name
        this.price = price
        this.imageUrl = imageUrl
    }
}
