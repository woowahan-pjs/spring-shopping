package shopping.member.domain;

import java.lang.reflect.Field;

public final class MemberFixture {
    private MemberFixture() {
    }

    public static Member member(String email, String password, MemberStatus status, MemberRole role) {
        return member(null, email, password, status, role);
    }

    public static Member member(Long memberId, String email, String password, MemberStatus status, MemberRole role) {
        Member member = Member.create(email, password, status, role);
        if (memberId != null) {
            setMemberId(member, memberId);
        }
        return member;
    }

    private static void setMemberId(Member member, Long memberId) {
        try {
            Field field = Member.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(member, memberId);
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Failed to set member id", exception);
        }
    }
}
