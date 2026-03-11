package shopping.storage.db

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import shopping.domain.Product

@Entity
@Table(name = "products")
class ProductEntity(
    @Column(nullable = false, length = 15)
    var name: String,
    @Column(nullable = false)
    var price: Int,
    @Column(nullable = false)
    var imageUrl: String,
) : BaseEntity() {
    fun toDomain(): Product =
        Product(
            id = this.id,
            name = this.name,
            price = this.price,
            imageUrl = this.imageUrl,
        )

    fun update(product: Product) {
        this.name = product.name.value
        this.price = product.price.value
        this.imageUrl = product.imageUrl
    }

    companion object {
        fun from(product: Product): ProductEntity =
            ProductEntity(
                name = product.name.value,
                price = product.price.value,
                imageUrl = product.imageUrl,
            )
    }
}
