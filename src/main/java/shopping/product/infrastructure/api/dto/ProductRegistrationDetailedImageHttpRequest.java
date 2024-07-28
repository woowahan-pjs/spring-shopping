package shopping.product.infrastructure.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ProductRegistrationDetailedImageHttpRequest {
    @JsonProperty("detailed_image_url")
    private List<String> images;

    public ProductRegistrationDetailedImageHttpRequest() {
    }

    public ProductRegistrationDetailedImageHttpRequest(final List<String> images) {
        this.images = images;
    }

    public List<String> getImages() {
        return images;
    }
}