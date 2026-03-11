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

    // 상품
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, ErrorCode.E2000, "요청하신 상품을 찾을 수 없어요.", LogLevel.INFO),
    PRODUCT_NAME_LENGTH_EXCEEDED(HttpStatus.BAD_REQUEST, ErrorCode.E2001, "상품명은 공백을 포함하여 최대 15자까지 입력할 수 있어요.", LogLevel.INFO),
    PRODUCT_NAME_INVALID_CHARACTERS(HttpStatus.BAD_REQUEST, ErrorCode.E2002, "상품명에 사용할 수 없는 특수문자가 포함되어 있어요.", LogLevel.INFO),
    PRODUCT_PROFANITY_DETECTED(HttpStatus.BAD_REQUEST, ErrorCode.E2003, "상품명에 비속어를 포함할 수 없어요. 바른 말을 사용해 주세요.", LogLevel.INFO),

    // 위시 리스트
    WISH_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, ErrorCode.E3000, "위시 리스트에서 해당 상품을 찾을 수 없어요.", LogLevel.INFO),
    WISH_ACCESS_DENIED(
        HttpStatus.FORBIDDEN,
        ErrorCode.E3001,
        "본인이 추가한 위시 리스트 내역만 조회하거나 삭제할 수 있어요.",
        LogLevel.WARN,
    ),

    // 외부 API 연동 - 비속어 필터링
    PURGOMALUM_API_ERROR(
        HttpStatus.BAD_GATEWAY,
        ErrorCode.E10000,
        "비속어 필터링 서비스 연결에 실패했어요. 잠시 후 다시 시도해주세요.",
        LogLevel.ERROR,
    ),
}
