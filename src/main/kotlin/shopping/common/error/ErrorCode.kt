package shopping.common.error

enum class ErrorCode(
    val code: String,
) {
    VALIDATION_ERROR("C0001"),
    UNKNOWN_ERROR("C0002"),

    PRODUCT_NOT_FOUND("P0000"),
    PRODUCT_NAME_CONTAIN_BAD_WORD("P0001"),
}
