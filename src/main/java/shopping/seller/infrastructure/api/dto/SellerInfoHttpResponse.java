package shopping.seller.infrastructure.api.dto;

public class SellerInfoHttpResponse {
    private String name;

    public SellerInfoHttpResponse() {
    }

    public SellerInfoHttpResponse(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
