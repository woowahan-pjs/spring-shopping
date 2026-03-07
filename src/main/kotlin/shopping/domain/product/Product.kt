package shopping.domain.product

import jakarta.persistence.Entity
import jakarta.persistence.Table
import shopping.support.config.jpa.BaseEntity
import java.math.BigDecimal
import java.time.LocalDateTime

@Table(name = "products")
@Entity
class Product(
    name: String,
    price: BigDecimal,
    imageUrl: String
) : BaseEntity() {
    var name: String = name
        protected set
    var price: BigDecimal = price
        protected set
    var imageUrl: String = imageUrl
        protected set

}