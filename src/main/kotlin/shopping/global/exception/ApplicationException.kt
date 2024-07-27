package shopping.global.exception

class ApplicationException @JvmOverloads constructor(
    val errorCode: ErrorCode,
    override val cause: Throwable? = null,
    message: String? = null,
) : RuntimeException() {
    override val message: String = message ?: errorCode.message
}
