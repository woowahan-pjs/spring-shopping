package shopping.product.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import shopping.common.domain.BaseEntity
import java.time.LocalDateTime

const val MAX_PRODUCT_NAME_LENGTH: Int = 15
const val PRODUCT_NAME_PATTERN: String = "^[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9 ()\\[\\]\\+\\-&/_]*$"

@SQLDelete(sql = "update product set deleted_at = CURRENT_TIMESTAMP where id = ?")
@SQLRestriction(value = "deleted_at is null")
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

    @Column
    var deletedAt: LocalDateTime? = null
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
