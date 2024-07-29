package shopping.customer.infrastructure.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CustomerSignInHttpResponse {

    @JsonProperty("access_token")
    private String accessToken;

    public CustomerSignInHttpResponse() {
    }

    public CustomerSignInHttpResponse(final String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}