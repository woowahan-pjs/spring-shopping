package shopping.api.presentation

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.logging.LogLevel
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import shopping.support.error.CoreException
import shopping.support.response.ApiResponse

@RestControllerAdvice
class ApiControllerAdvice {
    private val log: Logger = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(CoreException::class)
    fun handleCoreException(e: CoreException): ResponseEntity<ApiResponse<Any>> {
        getLogByLevel(e)
        return ResponseEntity(ApiResponse.Companion.error(e.errorType, e.data), e.errorType.status)
    }

    private fun getLogByLevel(e: CoreException) {
        if (e.errorType.logLevel == LogLevel.ERROR) {
            log.error("CoreException : {}", e.message, e)
            return
        }
        if (e.errorType.logLevel == LogLevel.WARN) {
            log.warn("CoreException : {}", e.message, e)
            return
        }
        log.info("CoreException : {}", e.message)
    }
}
