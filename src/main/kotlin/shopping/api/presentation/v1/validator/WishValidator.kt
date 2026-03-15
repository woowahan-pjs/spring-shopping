package shopping.api.presentation.v1.validator

import org.springframework.stereotype.Component
import shopping.api.presentation.v1.dto.CreateWishRequest
import shopping.support.error.CoreException
import shopping.support.error.ErrorType

@Component
class WishValidator {
    fun validateCreate(request: CreateWishRequest) {
        if (request.productId <= 0) {
            throw CoreException(ErrorType.INVALID_REQUEST, "유효하지 않은 상품 ID예요.")
        }
    }
}
