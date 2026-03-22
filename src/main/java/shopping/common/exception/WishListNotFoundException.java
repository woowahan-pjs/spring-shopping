package shopping.common.exception;

public class WishListNotFoundException extends RuntimeException{

    public WishListNotFoundException() {
        super("존재하지 않는 위시리스트입니다.");
    }
}
