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
        "알 수 없는 오류가 발생했어요. 잠시 후 다시 시도해주세요.",
        LogLevel.ERROR,
    ),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, ErrorCode.E400, "잘못된 요청이에요. 입력값을 확인해주세요.", LogLevel.INFO),

    // 회원 및 인증
    MEMBER_DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, ErrorCode.E1000, "이미 가입된 이메일이에요.", LogLevel.INFO),
    MEMBER_NOT_FOUND_OR_INVALID_PASSWORD(HttpStatus.BAD_REQUEST, ErrorCode.E1001, "가입되지 않은 이메일이거나 비밀번호가 일치하지 않아요.", LogLevel.INFO),
    UNAUTHORIZED_TOKEN(HttpStatus.UNAUTHORIZED, ErrorCode.E1002, "접근 권한이 없어요. 다시 로그인해 주세요.", LogLevel.INFO),

        LogLevel.ERROR,
    ),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, ErrorCode.E400, "잘못된 요청입니다. 입력값을 확인해주세요.", LogLevel.INFO),
    NOT_FOUND_DATA(HttpStatus.BAD_REQUEST, ErrorCode.E401, "요청하신 데이터를 찾을 수 없습니다.", LogLevel.ERROR),
}
