package shopping.wish.presentation.dto.request

import jakarta.validation.constraints.NotNull
import shopping.wish.application.command.WishProductAddCommand

data class WishProductAddRequest(
    @field:NotNull(message = "상품 ID 를 입력해주세요.")
    val productId: Long?,
) {
    fun toCommand(): WishProductAddCommand = WishProductAddCommand(productId!!)
}
