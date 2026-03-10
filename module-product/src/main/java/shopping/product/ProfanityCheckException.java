package shopping.product;

public class ProfanityCheckException extends RuntimeException {

    public ProfanityCheckException(Throwable cause) {
        super("비속어 검사에 실패했습니다.", cause);
    }
}
