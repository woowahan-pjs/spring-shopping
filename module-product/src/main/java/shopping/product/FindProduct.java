package shopping.product;

import java.util.List;

public interface FindProduct {

    Product execute(Long id);

    List<Product> execute();
}
