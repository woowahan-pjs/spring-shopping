package shopping.wish.service;

import shopping.wish.domain.*;
import shopping.member.domain.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.NoSuchElementException;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import shopping.member.service.InMemoryMemberRepository;
import shopping.member.domain.Member;

class AddWishServiceTest {

    private InMemoryMemberRepository memberRepository;
    private AddWishService service;

    @BeforeEach
    void setUp() {
        memberRepository = new InMemoryMemberRepository();
        service = new AddWishService(memberRepository);
    }

    @Test
    void 위시리스트에_상품을_추가한다() {
        UUID productId = UUID.randomUUID();
        Member member = memberRepository.save(new Member("test@test.com", "password"));

        Wish wish = service.execute(member.getId(), productId);

        assertEquals(productId, wish.getProductId());
        assertEquals(1, memberRepository.findById(member.getId()).orElseThrow().getWishes().size());
    }

    @Test
    void 존재하지_않는_회원이면_예외가_발생한다() {
        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> service.execute(UUID.randomUUID(), UUID.randomUUID()));

        assertEquals("회원을 찾을 수 없습니다.", exception.getMessage());
    }

    @Test
    void 이미_추가된_상품이면_예외가_발생한다() {
        UUID productId = UUID.randomUUID();
        Member member = memberRepository.save(new Member("test@test.com", "password"));
        service.execute(member.getId(), productId);

        assertThrows(IllegalArgumentException.class,
                () -> service.execute(member.getId(), productId));
    }
}
