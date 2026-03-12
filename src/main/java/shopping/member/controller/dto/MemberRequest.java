package shopping.member.controller.dto;

import shopping.member.domain.Member;

public class MemberRequest {
    private String email;
    private String password;

    public MemberRequest() {
    }

    public MemberRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Member toMember() {
        return new Member(email, password);
    }
}
