package shopping.domain

import shopping.support.error.CoreException
import shopping.support.error.ErrorType

class Wish(
    val id: Long = 0L,
    val memberId: Long,
    val productId: Long,
) {
    fun validateOwnership(requestMemberId: Long) {
        if (this.memberId != requestMemberId) {
            throw CoreException(ErrorType.WISH_ACCESS_DENIED)
        }
    }
}
