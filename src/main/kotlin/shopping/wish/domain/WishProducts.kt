package shopping.wish.domain

import jakarta.persistence.Embeddable
import jakarta.persistence.OneToMany
import shopping.global.exception.ApplicationException
import shopping.global.exception.ErrorCode

@Embeddable
class WishProducts(
    @field:OneToMany(mappedBy = "wishList")
    private val wishProducts: MutableSet<WishProduct> = mutableSetOf()
) : Iterable<WishProduct> by wishProducts {
    fun add(wishProduct: WishProduct) {
        this.wishProducts.add(wishProduct)
    }

    fun delete(wishProduct: WishProduct) {
        this.wishProducts.find(wishProduct::isSameWishProduct)?.let {
            this.wishProducts.remove(it)
            return
        }

        throw ApplicationException(ErrorCode.WISH_PRODUCT_NOT_FOUND)
    }
}
