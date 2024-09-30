package shopping.wish.presentation.dto.request

import jakarta.validation.constraints.NotNull
import shopping.wish.application.command.WishProductDeleteCommand

data class WishProductDeleteRequest(
    @field:NotNull(message = "상품 ID 를 입력해주세요.")
    val productId: Long?,
) {
    fun toCommand(): WishProductDeleteCommand = WishProductDeleteCommand(productId!!)
}
