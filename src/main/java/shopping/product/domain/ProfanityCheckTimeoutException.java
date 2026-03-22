package shopping.product.domain;

public class ProfanityCheckTimeoutException extends RuntimeException {

    public ProfanityCheckTimeoutException(Throwable cause) {
        super("비속어 검사 시간이 초과되었습니다.", cause);
    }
}
