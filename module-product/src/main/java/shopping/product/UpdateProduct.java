package shopping.product;

import java.util.UUID;

public interface UpdateProduct {

    Product execute(UUID id, String name, long price, String imageUrl);
}
