package shopping.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenRequest {
    private String email;
    private String password;

    public TokenRequest() {
    }

    public TokenRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static TokenRequest of(String email, String password) {
        return new TokenRequest(email, password);
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
