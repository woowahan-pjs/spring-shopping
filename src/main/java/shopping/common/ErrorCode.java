package shopping.common;

public enum ErrorCode {
    INVALID_PRODUCT_NAME_LENGTH(400, "상품의 이름은 15자까지 입력 가능합니다."),
    INVALID_PRODUCT_NAME_PATTERN(400, "상품 이름에 특수문자는 ( ) [ ] + - & / _ 만 사용 가능 합니다."),
    CONTAINS_PROFANITY(400, "상품 이름에 비속어는 포함될 수 없습니다"),
    ;

    private final int statusCode;
    private final String message;

    ErrorCode(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
