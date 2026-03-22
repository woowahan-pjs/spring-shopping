package shopping.product.domain;

import shopping.product.dto.ProductResponse;

import java.util.List;
import java.util.UUID;

public interface FindProduct {

    ProductResponse execute(UUID id);

    List<ProductResponse> execute();

    List<ProductResponse> execute(List<UUID> ids);
}
