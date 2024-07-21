package shopping.product.domain;

public interface ProductRepository {
    Product save(final ProductRegistrationRequest productRegistrationRequest);
}
