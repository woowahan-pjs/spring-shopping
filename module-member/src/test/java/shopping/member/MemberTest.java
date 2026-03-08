package shopping.member;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import shopping.wish.Wish;

class MemberTest {

    private final FakePasswordEncoder passwordEncoder = new FakePasswordEncoder();

    @Test
    void ID_없이_생성하면_ID가_null이다() {
        Member member = new Member("test@test.com", "password");

        assertNull(member.getId());
        assertEquals("test@test.com", member.getEmail());
    }

    @Test
    void ID와_함께_생성한다() {
        Member member = new Member(1L, "test@test.com", "password");

        assertEquals(1L, member.getId());
    }

    @Test
    void 올바른_비밀번호로_로그인한다() {
        Member member = new Member("test@test.com", passwordEncoder.encode("password"));

        member.login("password", passwordEncoder);
    }

    @Test
    void 잘못된_비밀번호로_로그인하면_예외가_발생한다() {
        Member member = new Member("test@test.com", passwordEncoder.encode("password"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> member.login("wrong", passwordEncoder));

        assertEquals("이메일 또는 비밀번호가 잘못되었습니다.", exception.getMessage());
    }

    @Test
    void 위시리스트에_상품을_추가한다() {
        Member member = new Member(1L, "test@test.com", "password");

        Wish wish = member.wish(100L);

        assertNotNull(wish);
        assertEquals(100L, wish.getProductId());
        assertEquals(1, member.getWishes().size());
    }

    @Test
    void 이미_추가된_상품을_위시리스트에_추가하면_예외가_발생한다() {
        Member member = new Member(1L, "test@test.com", "password");
        member.wish(100L);

        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> member.wish(100L));

        assertEquals("이미 위시리스트에 추가된 상품입니다.", exception.getMessage());
    }

    @Test
    void 위시리스트에서_상품을_제거한다() {
        Member member = new Member(1L, "test@test.com", "password");
        member.wish(100L);

        member.removeWish(100L);

        assertTrue(member.getWishes().isEmpty());
    }

    @Test
    void 위시리스트는_불변_리스트를_반환한다() {
        Member member = new Member(1L, "test@test.com", "password");

        assertThrows(UnsupportedOperationException.class,
                () -> member.getWishes().add(new Wish(1L)));
    }
}
