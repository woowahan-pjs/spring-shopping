package shopping.product.service.dto;

import shopping.product.domain.Price;
import shopping.product.domain.Product;

public record ProductRegisterInput(String name, Long price, String imageUrl) {
    public Product toDomain() {
        return Product.builder()
                      .name(name)
                      .price(new Price(price))
                      .imageUrl(imageUrl)
                      .build();
    }
}
