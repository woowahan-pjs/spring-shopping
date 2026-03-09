package shopping.wish;

import java.util.UUID;

public interface AddWish {

    Wish execute(UUID memberId, UUID productId);
}
