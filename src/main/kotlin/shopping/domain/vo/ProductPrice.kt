package shopping.domain.vo

import shopping.support.error.CoreException
import shopping.support.error.ErrorType

data class ProductPrice(
    val value: Int,
) {
    init {
        validatePrice(value)
    }

    private fun validatePrice(price: Int) {
        if (price < 0) {
            throw CoreException(ErrorType.INVALID_REQUEST, "상품 가격은 0원 이상이어야 해요.")
        }
    }
}
