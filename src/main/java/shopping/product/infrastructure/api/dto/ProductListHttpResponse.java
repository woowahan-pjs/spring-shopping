package shopping.product.infrastructure.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ProductListHttpResponse {
    @JsonProperty("products")
    private List<ProductList> productLists;

    public ProductListHttpResponse() {
    }

    public ProductListHttpResponse(final List<ProductList> productLists) {
        this.productLists = productLists;
    }

    public List<ProductList> getProductLists() {
        return productLists;
    }
}
