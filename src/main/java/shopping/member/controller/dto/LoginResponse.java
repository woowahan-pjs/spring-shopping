package shopping.member.controller.dto;

public class LoginResponse {
    private String accessToken;

    public LoginResponse(String token) {
        this.accessToken = token;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
