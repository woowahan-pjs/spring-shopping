package shopping.common;

public enum ErrorCode {
    INVALID_PRODUCT_NAME_LENGTH(400, "상품의 이름은 15자까지 입력 가능합니다."),
    INVALID_PRODUCT_NAME_PATTERN(400, "상품 이름에 특수문자는 ( ) [ ] + - & / _ 만 사용 가능 합니다."),
    CONTAINS_PROFANITY(400, "상품 이름에 비속어는 포함될 수 없습니다"),
    INVALID_INPUT_ARGUMENTS(400, "입력 값이 비었거나 null일 수 없습니다."),
    PRODUCT_NOT_FOUND(400, "id에 해당하는 상품이 없습니다."),
    ALREADY_REGISTERED_EMAIL(400, "이미 가입된 이메일입니다."),
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
