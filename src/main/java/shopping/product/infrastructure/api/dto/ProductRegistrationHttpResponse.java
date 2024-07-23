package shopping.product.infrastructure.api.dto;

public class ProductRegistrationHttpResponse {
    private long id;

    public ProductRegistrationHttpResponse() {
    }

    public ProductRegistrationHttpResponse(final long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}