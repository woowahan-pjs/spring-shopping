package shopping.common.error

data class ErrorMessage(
    val code: ErrorCode,
    val message: String,
    val data: Any? = null,
)
