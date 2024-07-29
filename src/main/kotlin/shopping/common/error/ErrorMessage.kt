package shopping.common.error

data class ErrorMessage private constructor(
    val code: String,
    val message: String,
    val data: Any? = null,
) {
    constructor(errorCode: ErrorCode, message: String, data: Any? = null) : this(errorCode.code, message, data)
}
