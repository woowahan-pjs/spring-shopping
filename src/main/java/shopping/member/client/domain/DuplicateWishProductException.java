package shopping.member.client.domain;

public class DuplicateWishProductException extends RuntimeException {

    public DuplicateWishProductException(final String message) {
        super(message);
    }
}
