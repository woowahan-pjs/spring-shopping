package shopping.product.domain;

import java.util.UUID;

public interface DeleteProduct {

    void execute(UUID id);
}
