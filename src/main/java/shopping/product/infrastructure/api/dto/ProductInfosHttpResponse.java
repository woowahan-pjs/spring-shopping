package shopping.product.infrastructure.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ProductInfosHttpResponse {
    @JsonProperty("products")
    private List<ProductInfo> productInfos;

    public ProductInfosHttpResponse() {
    }

    public ProductInfosHttpResponse(final List<ProductInfo> productInfos) {
        this.productInfos = productInfos;
    }

    public List<ProductInfo> getProductLists() {
        return productInfos;
    }
}
