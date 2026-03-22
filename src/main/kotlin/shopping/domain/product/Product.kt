package shopping.domain.product

import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.hibernate.annotations.SQLDelete
import shopping.support.config.jpa.BaseEntity
import shopping.support.error.CoreException
import shopping.support.error.ErrorType
import java.math.BigDecimal

@Table(name = "products")
@Entity
@SQLDelete(sql = "UPDATE products SET deleted_at = NOW() WHERE id = ?")
class Product(
    name: String,
    price: BigDecimal,
    imageUrl: String,
) : BaseEntity() {
    var name: String = name
        protected set
    var price: BigDecimal = price
        protected set
    var imageUrl: String = imageUrl
        protected set

    override fun guard() {
        validateName(name)
    }

    fun update(
        name: String,
        price: BigDecimal,
        imageUrl: String,
    ) {
        validateName(name)
        this.name = name
        this.price = price
        this.imageUrl = imageUrl
    }

    companion object {
        private val NAME_PATTERN = Regex("^[a-zA-Z0-9가-힣 ()\\[\\]&,\\-+/_]{1,15}$")

        private fun validateName(name: String) {
            if (!NAME_PATTERN.matches(name)) {
                throw CoreException(ErrorType.BAD_REQUEST, "상품 이름은 공백 포함 최대 15자이며, 허용되지 않는 특수문자는 사용할 수 없습니다.")
            }
        }
    }
}
