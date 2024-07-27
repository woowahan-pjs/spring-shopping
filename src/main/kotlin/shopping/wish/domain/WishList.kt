package shopping.wish.domain

import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.Table
import shopping.global.common.BaseEntity

@Entity
@Table(name = "wish_list")
class WishList(
    memberId: Long
) : BaseEntity() {
    @field:Column(name = "member_id", nullable = false)
    var memberId: Long = memberId
        protected set

    @field:Embedded
    private val wishProducts: WishProducts = WishProducts()

    fun addWishProduct(wishProduct: WishProduct) {
        this.wishProducts.add(wishProduct)
    }

    fun deleteWishProduct(wishProduct: WishProduct) {
        this.wishProducts.delete(wishProduct)
    }

    fun getWishProducts(): Set<WishProduct> = wishProducts.toSet()
}
