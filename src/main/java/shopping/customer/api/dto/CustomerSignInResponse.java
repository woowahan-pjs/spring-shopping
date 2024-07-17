package shopping.customer.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CustomerSignInResponse {

    @JsonProperty("access_token")
    private String accessToken;

    public CustomerSignInResponse() {
    }

    public CustomerSignInResponse(final String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}