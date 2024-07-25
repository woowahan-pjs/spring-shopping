package shopping.global.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val status: HttpStatus,
    val message: String,
) {
    // INPUT VALIDATION
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다."),

    // MEMBER
    DUPLICATED_REGISTER_EMAIL(HttpStatus.CONFLICT, "이미 존재 하는 이메일 입니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "일치하는 회원 정보를 찾을 수 없습니다."),

    // SECURITY
    EMPTY_CLAIM(HttpStatus.UNAUTHORIZED, "CLAIM 정보가 비어있습니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "액세스 토큰이 유효하지 않습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 유효하지 않습니다."),
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "토큰을 찾을 수 없습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "토큰 인증에 실패하였습니다."),
    MISS_MATCH_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 일치하지 않습니다."),
    INVALID_TOKEN_REISSUE_REQUEST(HttpStatus.BAD_REQUEST, "토큰을 재발급 할 수 없습니다."),
    INVALID_USER_AUTH(HttpStatus.UNAUTHORIZED, "올바른 이메일 또는 비밀번호가 아닙니다."),

    // PRODUCT
    PROFANITY_CONTAIN_PRODUCT_NAME(HttpStatus.BAD_REQUEST, "상품 이름에 비속어가 포함되어 있습니다"),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."),

    // COMMON
    INTERNAL_SEVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "관리자에게 문의 바랍니다."),

}
