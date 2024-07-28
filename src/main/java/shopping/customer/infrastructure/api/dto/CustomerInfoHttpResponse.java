package shopping.customer.infrastructure.api.dto;

public class CustomerInfoHttpResponse {
    private String name;

    public CustomerInfoHttpResponse() {
    }

    public CustomerInfoHttpResponse(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

