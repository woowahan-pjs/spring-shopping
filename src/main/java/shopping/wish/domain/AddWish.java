package shopping.wish.domain;

import java.util.UUID;

public interface AddWish {

    Wish execute(UUID memberId, UUID productId, long wishedPrice);
}
