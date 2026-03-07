package shopping.member.dto;

public class MemberResponse {

    private String token;

    public MemberResponse() {}

    public MemberResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
