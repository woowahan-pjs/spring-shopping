package shopping.member.domain;

import shopping.member.service.FakePasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import shopping.wish.domain.Wish;

class MemberTest {

    private final FakePasswordEncoder passwordEncoder = new FakePasswordEncoder();

    @Test
    void ID_없이_생성하면_ID가_자동생성된다() {
        Member member = new Member("test@test.com", "password");

        assertNotNull(member.getId());
        assertEquals("test@test.com", member.getEmail());
    }

    @Test
    void ID와_함께_생성한다() {
        UUID id = UUID.randomUUID();
        Member member = new Member(id, "test@test.com", "password");

        assertEquals(id, member.getId());
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
        UUID productId = UUID.randomUUID();
        Member member = new Member("test@test.com", "password");

        Wish wish = member.wish(productId);

        assertNotNull(wish);
        assertEquals(productId, wish.getProductId());
        assertEquals(1, member.getWishes().size());
    }

    @Test
    void 이미_추가된_상품을_위시리스트에_추가하면_예외가_발생한다() {
        UUID productId = UUID.randomUUID();
        Member member = new Member("test@test.com", "password");
        member.wish(productId);

        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> member.wish(productId));

        assertEquals("이미 위시리스트에 추가된 상품입니다.", exception.getMessage());
    }

    @Test
    void 위시리스트에서_상품을_제거한다() {
        UUID productId = UUID.randomUUID();
        Member member = new Member("test@test.com", "password");
        member.wish(productId);

        member.removeWish(productId);

        assertTrue(member.getWishes().isEmpty());
    }

    @Test
    void 위시리스트는_불변_리스트를_반환한다() {
        Member member = new Member("test@test.com", "password");

        assertThrows(UnsupportedOperationException.class,
                () -> member.getWishes().add(new Wish(UUID.randomUUID())));
    }
}
