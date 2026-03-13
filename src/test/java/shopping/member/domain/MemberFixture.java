package shopping.member.domain;

public class MemberFixture {

    public static Member createMember() {
        return new Member("test@gmail.com", "password");
    }

    public static Member createWithId(Long id) {
        return Member.of(id, "test@gmail.com", "password");
    }
}
