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

        Wish wish = service.execute(member.getId(), productId, 50000L);

        assertEquals(productId, wish.getProductId());
        assertEquals(50000L, wish.getWishedPrice());
        assertEquals(1, memberRepository.findById(member.getId()).orElseThrow().getWishes().size());
    }

    @Test
    void 존재하지_않는_회원이면_예외가_발생한다() {
        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> service.execute(UUID.randomUUID(), UUID.randomUUID(), 10000L));

        assertEquals("회원을 찾을 수 없습니다.", exception.getMessage());
    }

    @Test
    void 이미_추가된_상품이면_기존_위시를_반환한다() {
        UUID productId = UUID.randomUUID();
        Member member = memberRepository.save(new Member("test@test.com", "password"));
        Wish first = service.execute(member.getId(), productId, 50000L);

        Wish second = service.execute(member.getId(), productId, 60000L);

        assertEquals(first.getId(), second.getId());
        assertEquals(50000L, second.getWishedPrice());
        assertEquals(1, memberRepository.findById(member.getId()).orElseThrow().getWishes().size());
    }
}
