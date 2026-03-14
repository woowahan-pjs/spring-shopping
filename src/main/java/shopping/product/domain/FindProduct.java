package shopping.product.domain;

import java.util.List;
import java.util.UUID;

public interface FindProduct {

    Product execute(UUID id);

    List<Product> execute();
}
