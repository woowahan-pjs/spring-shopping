package shopping.infra.exception;

public class ShoppingBusinessException extends RuntimeException {

    public ShoppingBusinessException() {
        super("알 수 없는 오류가 발생하였습니다.");
    }
}
