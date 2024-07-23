package shopping.admin.infrastructure.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AdminSignInHttpResponse {

    @JsonProperty("access_token")
    private String accessToken;

    public AdminSignInHttpResponse() {
    }

    public AdminSignInHttpResponse(final String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}