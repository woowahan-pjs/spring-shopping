package shopping.auth.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginMember {
    private Long id;
    private String email;

    public LoginMember() {
    }

    public LoginMember(Long id, String email) {
        this.id = id;
        this.email = email;
    }
}
