package shopping.member.controller.dto;

import shopping.member.domain.Member;

public record MemberRequest(String email, String password) {

    public Member toMember() {
        return new Member(email, password);
    }
}
