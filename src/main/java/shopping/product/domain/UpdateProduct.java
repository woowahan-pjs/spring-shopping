package shopping.product.domain;

import java.util.UUID;

public interface UpdateProduct {

    void execute(UUID id, String name, long price, String imageUrl);
}
