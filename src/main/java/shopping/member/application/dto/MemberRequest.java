package shopping.member.application.dto;


public class MemberRequest {
    private String email;
    private String password;

    public MemberRequest() {
    }

    public MemberRequest(final String email, final String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
