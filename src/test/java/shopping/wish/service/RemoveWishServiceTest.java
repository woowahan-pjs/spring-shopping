package shopping.wish.service;

import shopping.wish.domain.*;
import shopping.member.domain.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.NoSuchElementException;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import shopping.member.service.FakePasswordEncoder;
import shopping.member.service.InMemoryMemberRepository;
import shopping.member.domain.Member;
import shopping.member.domain.PasswordFactory;

class RemoveWishServiceTest {

    private InMemoryMemberRepository memberRepository;
    private PasswordFactory passwordFactory;
    private RemoveWishService service;

    @BeforeEach
    void setUp() {
        memberRepository = new InMemoryMemberRepository();
        passwordFactory = new PasswordFactory(new FakePasswordEncoder());
        service = new RemoveWishService(memberRepository);
    }

    @Test
    void 위시리스트에서_상품을_제거한다() {
        UUID productId = UUID.randomUUID();
        Member member = memberRepository
                .save(new Member("test@test.com", passwordFactory.create("password1234")));
        member.wish(productId, 50000L);
        memberRepository.save(member);

        service.execute(member.getId(), productId);

        assertTrue(memberRepository.findById(member.getId()).orElseThrow().getWishes().isEmpty());
    }

    @Test
    void 존재하지_않는_회원이면_예외가_발생한다() {
        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> service.execute(UUID.randomUUID(), UUID.randomUUID()));

        assertEquals("회원을 찾을 수 없습니다.", exception.getMessage());
    }

    @Test
    void 존재하지_않는_상품을_제거해도_예외가_발생하지_않는다() {
        Member member = memberRepository
                .save(new Member("test@test.com", passwordFactory.create("password1234")));

        service.execute(member.getId(), UUID.randomUUID());

        assertTrue(memberRepository.findById(member.getId()).orElseThrow().getWishes().isEmpty());
    }
}
