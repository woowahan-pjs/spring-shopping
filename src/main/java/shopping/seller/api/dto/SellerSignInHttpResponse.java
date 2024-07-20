package shopping.seller.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SellerSignInHttpResponse {

    @JsonProperty("access_token")
    private String accessToken;

    public SellerSignInHttpResponse() {
    }

    public SellerSignInHttpResponse(final String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}