package shopping.product.domain;

import shopping.product.dto.CreateProductResponse;

public interface CreateProduct {

    CreateProductResponse execute(String name, long price, String imageUrl);
}
