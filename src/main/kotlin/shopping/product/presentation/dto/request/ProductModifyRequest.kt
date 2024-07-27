package shopping.product.presentation.dto.request

import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import shopping.product.application.command.ProductUpdateCommand
import java.math.BigDecimal

data class ProductModifyRequest(
    @field:NotNull(message = "상품 판매 가격을 입력해주세요.")
    @field:DecimalMax(value = "999999999", message = "상품 판매 가격은 최대 999,999,999 입니다.")
    @field:DecimalMin(value = "0", message = "상품 판매 가격은 음수일 수 없습니다.")
    val sellingPrice: BigDecimal?,
    @field:NotNull(message = "상품 정가를 입력해주세요.")
    @field:DecimalMax(value = "999999999", message = "상품 정가는 최대 999,999,999 입니다.")
    @field:DecimalMin(value = "0", message = "상품 정가는 음수일 수 없습니다.")
    val fixedPrice: BigDecimal?,
    @field:NotBlank(message = "상품 이름을 입력해주세요.")
    @field:Size(max = 15, message = "상품 이름은 최대 15자 까지 입력할 수 있습니다.")
    @field:Pattern(
        regexp = "^[a-zA-Z0-9ㄱ-ㅎ가-힣 ()\\[\\]+\\-&/_]*$",
        message = "상품 이름은 한글, 영어, 숫자, 허용 된 특수문자((, ), [, ], +, -, &, /, _) 사용 가능합니다."
    )
    val productName: String?,
    @field:NotNull(message = "상품 재고를 입력해주세요.")
    @field:Min(value = 0, message = "상품 개수는 음수일 수 없습니다.")
    val productAmount: Int?,
    @field:NotBlank(message = "상품 이미지를 등록해주세요.")
    val productImage: String?,
    val productDescription: String?,
) {
    fun toCommand(): ProductUpdateCommand =
        ProductUpdateCommand(
            sellingPrice!!,
            fixedPrice!!,
            productName!!,
            productAmount!!,
            productImage!!,
            productDescription!!,
        )
}
