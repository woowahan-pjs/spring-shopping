package shopping.domain

import shopping.domain.vo.ProductName
import shopping.domain.vo.ProductPrice

class Product(
    val id: Long = 0L,
    name: String,
    price: Int,
    imageUrl: String,
) {
    var name: ProductName = ProductName(name)
        private set
    var price: ProductPrice = ProductPrice(price)
        private set
    var imageUrl: String = imageUrl
        private set

    fun update(
        name: String,
        price: Int,
        imageUrl: String,
    ) {
        this.name = ProductName(name)
        this.price = ProductPrice(price)
        this.imageUrl = imageUrl
    }
}
