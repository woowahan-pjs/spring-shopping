package shopping.common;

public enum ErrorCode {
    // 400
    INVALID_PRODUCT_NAME_LENGTH(400, "상품의 이름은 15자까지 입력 가능합니다."),
    INVALID_PRODUCT_NAME_PATTERN(400, "상품 이름에 특수문자는 ( ) [ ] + - & / _ 만 사용 가능 합니다."),
    CONTAINS_PROFANITY(400, "상품 이름에 비속어는 포함될 수 없습니다"),
    INVALID_INPUT_ARGUMENTS(400, "입력 값이 비었거나 null일 수 없습니다."),
    PRODUCT_NOT_FOUND(400, "id에 해당하는 상품이 없습니다."),
    ALREADY_REGISTERED_EMAIL(400, "이미 가입된 이메일입니다."),
    ALREADY_REGISTERED_WISH_PRODUCT(400, "위시 리스트에 이미 등록된 상품입니다."),

    // 401
    NOT_MATCHED_PASSWORD(401, "비밀번호가 일치하지 않습니다."),
    REQUIRES_TOKEN(401, "토큰이 필요합니다."),
    INVALID_TOKEN(401, "유효하지 않은 토큰 형식입니다."),

    // 404
    MEMBER_NOT_FOUND(404, "가입되지 않은 이메일 입니다."),
    ;

    private final int status;
    private final String message;

    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
