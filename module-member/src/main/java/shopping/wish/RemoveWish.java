package shopping.wish;

import java.util.UUID;

public interface RemoveWish {

    void execute(UUID memberId, UUID productId);
}
