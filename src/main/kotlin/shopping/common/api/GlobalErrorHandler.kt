package shopping.common.api

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import shopping.common.error.ApiException
import shopping.common.error.ErrorCode
import shopping.common.error.ErrorMessage

@RestControllerAdvice
class GlobalErrorHandler {
    private val logger: Logger = LoggerFactory.getLogger(GlobalErrorHandler::class.java)

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun httpMessageNotReadableExceptionHandler(e: HttpMessageNotReadableException): ResponseEntity<ApiResponse<Unit>> {
        logger.error("HttpMessageNotReadableException: {}", e.message, e)
        val data =
            when (val causeException = e.cause) {
                is MismatchedInputException -> causeException.path[0].toData()
                else -> null
            }

        val errorMessage =
            ErrorMessage(
                code = ErrorCode.VALIDATION_ERROR,
                message = e.message ?: "Missing field error",
                data = data,
            )

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error(errorMessage))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun requestValidationExceptionHandler(e: MethodArgumentNotValidException): ResponseEntity<ApiResponse<Unit>> {
        logger.error("MethodArgumentNotValidException: {}", e.message, e)
        val errorMessage =
            ErrorMessage(
                code = ErrorCode.VALIDATION_ERROR,
                message = e.message,
                data = e.bindingResult.fieldErrors.map { it.toData() },
            )

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error(errorMessage))
    }

    @ExceptionHandler(ApiException::class)
    fun apiExceptionHandler(e: ApiException): ResponseEntity<ApiResponse<Unit>> {
        logger.warn("ApiException: {}", e.message, e)
        val errorMessage =
            ErrorMessage(
                code = e.errorCode,
                message = e.message,
                data = e.data,
            )

        return ResponseEntity
            .ok()
            .body(ApiResponse.error(errorMessage))
    }

    @ExceptionHandler(Exception::class)
    fun exceptionHandler(e: Exception): ResponseEntity<ApiResponse<Unit>> {
        logger.error("Exception: {}", e.message, e)
        val errorMessage =
            ErrorMessage(
                code = ErrorCode.UNKNOWN_ERROR,
                message = e.message ?: "Unknown Error",
            )

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.error(errorMessage))
    }
}

private fun FieldError.toData() =
    mapOf(
        "field" to field,
        "rejectedValue" to rejectedValue,
        "message" to defaultMessage,
    )

private fun JsonMappingException.Reference.toData() =
    mapOf(
        "field" to fieldName,
        "rejectedValue" to null,
        "message" to "null 이면 안됩니다.",
    )
