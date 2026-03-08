package shopping.support.error

import org.springframework.boot.logging.LogLevel
import org.springframework.http.HttpStatus

enum class ErrorType(
    val status: HttpStatus,
    val code: ErrorCode,
    val message: String,
    val logLevel: LogLevel,
) {
    // 공통
    DEFAULT_ERROR(
        HttpStatus.INTERNAL_SERVER_ERROR,
        ErrorCode.E500,
        "알 수 없는 오류가 발생했습니다. 잠시 후 다시 시도해주세요.",
        LogLevel.ERROR,
    ),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, ErrorCode.E400, "잘못된 요청입니다. 입력값을 확인해주세요.", LogLevel.INFO),
    NOT_FOUND_DATA(HttpStatus.BAD_REQUEST, ErrorCode.E401, "요청하신 데이터를 찾을 수 없습니다.", LogLevel.ERROR),
}
