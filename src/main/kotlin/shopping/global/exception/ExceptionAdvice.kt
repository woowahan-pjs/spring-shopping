package shopping.global.exception

import io.github.oshai.kotlinlogging.KotlinLogging
import org.slf4j.Logger
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.InsufficientAuthenticationException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import shopping.global.common.ErrorResponse
import javax.naming.AuthenticationException

@RestControllerAdvice
class ExceptionAdvice {

    @ExceptionHandler(ApplicationException::class)
    fun handleApplicationException(e: ApplicationException): ResponseEntity<ErrorResponse> =
        ErrorResponseEntity(e.errorCode, e)

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> =
        ErrorResponseEntity(ErrorCode.INVALID_INPUT_VALUE, e, e.bindingResult.fieldErrors[0].defaultMessage)

    @ExceptionHandler(AuthenticationException::class)
    fun handleAuthenticationException(e: AuthenticationException): ResponseEntity<ErrorResponse> =
        ErrorResponseEntity(ErrorCode.UNAUTHORIZED, e)

    @ExceptionHandler(InsufficientAuthenticationException::class)
    fun handleInsufficientAuthenticationException(e: InsufficientAuthenticationException): ResponseEntity<ErrorResponse> =
        ErrorResponseEntity(ErrorCode.UNAUTHORIZED, e)

    @ExceptionHandler(BadCredentialsException::class)
    fun handleBadCredentialsException(e: BadCredentialsException): ResponseEntity<ErrorResponse> =
        ErrorResponseEntity(ErrorCode.INVALID_USER_AUTH, e)

    @ExceptionHandler(Throwable::class)
    fun handleThrowable(t: Throwable): ResponseEntity<ErrorResponse> =
        ErrorResponseEntity(ErrorCode.INTERNAL_SEVER_ERROR, t)

    private fun ErrorResponseEntity(errorCode: ErrorCode, cause: Throwable, message: String? = null,): ResponseEntity<ErrorResponse> {
        log.error {
            """
                server error
                cause: $cause
                message: ${message ?: errorCode.message}
                errorCode: $errorCode
            """.trimIndent()
        }
        return ResponseEntity(ErrorResponse(errorCode, message), errorCode.status)
    }

    companion object {
        private val log = KotlinLogging.logger { }
    }
}
