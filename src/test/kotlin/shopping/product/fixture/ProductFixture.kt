package shopping.product.fixture

import shopping.product.application.command.ProductCreateCommand
import shopping.product.application.command.ProductUpdateCommand
import shopping.product.domain.Product
import shopping.product.presentation.dto.request.ProductModifyRequest
import shopping.product.presentation.dto.request.ProductRegisterRequest
import java.math.BigDecimal

enum class ProductFixture(
    val sellingPrice: BigDecimal?,
    val fixedPrice: BigDecimal?,
    val productName: String?,
    val productAmount: Int?,
    val productImage: String?,
    val productDescription: String?,
) {
    // 정상 상품
    `상품 1`(BigDecimal("1000000.00"), BigDecimal("1000000.00"), "시원한 에어컨", 100, "https://image.com", "시원한 에어컨 이에용"),
    `상품 2`(BigDecimal("500000.00"), BigDecimal("1000000.00"), "시원한 에어컨 - 할인가", 50, "https://image.com", "시원한 에어컨 할인 제품 이에용"),

    // 비정상 상품
    `비속어 상품`(BigDecimal("1000000.00"), BigDecimal("1000000.00"), "fucking 시원한 에어컨", 100, "https://image.com", "시원한 에어컨 이에용"),
    `상품 판매 가격 NULL 상품`(null, BigDecimal("1000000.00"), "시원한 에어컨", 100, "https://image.com", "시원한 에어컨 이에용"),
    `상품 판매 가격 최대값 초과 상품`(BigDecimal("1000000000.00"), BigDecimal("1000000.00"), "시원한 에어컨", 100, "https://image.com", "시원한 에어컨 이에용"),
    `상품 판매 가격 최소값 미만 상품`(BigDecimal("-0.01"), BigDecimal("1000000.00"), "시원한 에어컨", 100, "https://image.com", "시원한 에어컨 이에용"),
    `상품 정가 NULL 상품`(BigDecimal("1000000.00"), null, "시원한 에어컨", 100, "https://image.com", "시원한 에어컨 이에용"),
    `상품 정가 최대값 초과 상품`(BigDecimal("1000000.00"), BigDecimal("1000000000.00"), "시원한 에어컨", 100, "https://image.com", "시원한 에어컨 이에용"),
    `상품 정가 최소값 미만 상품`(BigDecimal("1000000.00"), BigDecimal("-0.01"), "시원한 에어컨", 100, "https://image.com", "시원한 에어컨 이에용"),
    `상품 이름 공백 상품`(BigDecimal("1000000.00"), BigDecimal("1000000.00"), "", 100, "https://image.com", "시원한 에어컨 이에용"),
    `상품 이름 NULL 상품`(BigDecimal("1000000.00"), BigDecimal("1000000.00"), null, 100, "https://image.com", "시원한 에어컨 이에용"),
    `상품 이름 최대 길이 초과 상품`(BigDecimal("1000000.00"), BigDecimal("1000000.00"), "시원한 에어컨이 왔어요 시원한 에어컨", 100, "https://image.com", "시원한 에어컨 이에용"),
    `상품 이름 허용 특수 문자 상품1`(BigDecimal("1000000.00"), BigDecimal("1000000.00"), "시원한 에어컨(", 100, "https://image.com", "시원한 에어컨 이에용"),
    `상품 이름 허용 특수 문자 상품2`(BigDecimal("1000000.00"), BigDecimal("1000000.00"), "시원한 에어컨)", 100, "https://image.com", "시원한 에어컨 이에용"),
    `상품 이름 허용 특수 문자 상품3`(BigDecimal("1000000.00"), BigDecimal("1000000.00"), "시원한 에어컨[", 100, "https://image.com", "시원한 에어컨 이에용"),
    `상품 이름 허용 특수 문자 상품4`(BigDecimal("1000000.00"), BigDecimal("1000000.00"), "시원한 에어컨]", 100, "https://image.com", "시원한 에어컨 이에용"),
    `상품 이름 허용 특수 문자 상품5`(BigDecimal("1000000.00"), BigDecimal("1000000.00"), "시원한 에어컨+", 100, "https://image.com", "시원한 에어컨 이에용"),
    `상품 이름 허용 특수 문자 상품6`(BigDecimal("1000000.00"), BigDecimal("1000000.00"), "시원한 에어컨-", 100, "https://image.com", "시원한 에어컨 이에용"),
    `상품 이름 허용 특수 문자 상품7`(BigDecimal("1000000.00"), BigDecimal("1000000.00"), "시원한 에어컨&", 100, "https://image.com", "시원한 에어컨 이에용"),
    `상품 이름 허용 특수 문자 상품8`(BigDecimal("1000000.00"), BigDecimal("1000000.00"), "시원한 에어컨/", 100, "https://image.com", "시원한 에어컨 이에용"),
    `상품 이름 허용 특수 문자 상품9`(BigDecimal("1000000.00"), BigDecimal("1000000.00"), "시원한 에어컨_", 100, "https://image.com", "시원한 에어컨 이에용"),
    `상품 이름 허용되지 않은 특수 문자 상품`(BigDecimal("1000000.00"), BigDecimal("1000000.00"), "시원한 에어컨!", 100, "https://image.com", "시원한 에어컨 이에용"),
    `상품 재고 NULL 상품`(BigDecimal("1000000.00"), BigDecimal("1000000.00"), "시원한 에어컨", null, "https://image.com", "시원한 에어컨 이에용"),
    `상품 재고 음수 상품`(BigDecimal("1000000.00"), BigDecimal("1000000.00"), "시원한 에어컨", -1, "https://image.com", "시원한 에어컨 이에용"),
    `상품 이미지 NULL 상품`(BigDecimal("1000000.00"), BigDecimal("1000000.00"), "시원한 에어컨", 100, null, "시원한 에어컨 이에용"),
    ;

    fun `상품 엔티티 생성`(): Product = Product(sellingPrice!!, fixedPrice!!, productName!!, productAmount!!, productImage!!, productDescription!!)
    fun `상품 생성 COMMAND 생성`(): ProductCreateCommand = ProductCreateCommand(sellingPrice!!, fixedPrice!!, productName!!, productAmount!!, productImage!!, productDescription!!)
    fun `상품 수정 COMMAND 생성`(): ProductUpdateCommand = ProductUpdateCommand(sellingPrice!!, fixedPrice!!, productName!!, productAmount!!, productImage!!, productDescription!!)
    fun `상품 등록 요청 DTO 생성`(): ProductRegisterRequest = ProductRegisterRequest(sellingPrice, fixedPrice, productName, productAmount, productImage, productDescription)
    fun `상품 수정 요청 DTO 생성`(): ProductModifyRequest = ProductModifyRequest(sellingPrice, fixedPrice, productName, productAmount, productImage, productDescription)
}
