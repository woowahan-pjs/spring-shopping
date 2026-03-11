package shopping.wish.domain;

import java.util.UUID;

public interface RemoveWishByProduct {

    void execute(UUID productId);
}
