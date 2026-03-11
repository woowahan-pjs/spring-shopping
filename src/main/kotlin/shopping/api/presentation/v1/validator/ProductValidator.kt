package shopping.api.presentation.v1.validator

import org.springframework.stereotype.Component
import shopping.api.presentation.v1.dto.CreateProductRequest
import shopping.api.presentation.v1.dto.UpdateProductRequest
import shopping.support.error.CoreException
import shopping.support.error.ErrorType

@Component
class ProductValidator {
    fun validateCreate(request: CreateProductRequest) {
        validateName(request.name)
        validateImageUrl(request.imageUrl)
    }

    fun validateUpdate(request: UpdateProductRequest) {
        validateName(request.name)
        validateImageUrl(request.imageUrl)
    }

    private fun validateName(name: String) {
        if (name.isBlank()) {
            throw CoreException(ErrorType.INVALID_REQUEST, "상품 이름을 입력해 주세요.")
        }
    }

    private fun validateImageUrl(imageUrl: String) {
        if (imageUrl.isBlank()) {
            throw CoreException(ErrorType.INVALID_REQUEST, "상품 이미지 URL을 입력해 주세요.")
        }
    }
}
