package shopping.common.api

import shopping.common.error.ErrorMessage

class ApiResponse<T> private constructor(
    val data: T? = null,
    val error: ErrorMessage? = null,
) {
    companion object {
        fun success(): ApiResponse<Unit> = ApiResponse()

        fun <T> success(data: T): ApiResponse<T> = ApiResponse(data = data)

        fun error(errorMessage: ErrorMessage?): ApiResponse<Unit> = ApiResponse(error = errorMessage)
    }
}
