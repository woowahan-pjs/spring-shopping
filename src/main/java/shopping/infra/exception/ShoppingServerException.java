package shopping.infra.exception;

public class ShoppingServerException extends RuntimeException {

    public ShoppingServerException() {
        super("알 수 없는 오류가 발생하였습니다.");
    }
}
