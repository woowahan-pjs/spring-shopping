package shopping.wish;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import shopping.member.InMemoryMemberRepository;
import shopping.member.Member;

class RemoveWishServiceTest {

    private InMemoryMemberRepository memberRepository;
    private RemoveWishService service;

    @BeforeEach
    void setUp() {
        memberRepository = new InMemoryMemberRepository();
        service = new RemoveWishService(memberRepository);
    }

    @Test
    void 위시리스트에서_상품을_제거한다() {
        Member member = memberRepository.save(new Member("test@test.com", "password"));
        member.wish(100L);
        memberRepository.save(member);

        service.execute(member.getId(), 100L);

        assertTrue(memberRepository.findById(member.getId()).orElseThrow().getWishes().isEmpty());
    }

    @Test
    void 존재하지_않는_회원이면_예외가_발생한다() {
        NoSuchElementException exception =
                assertThrows(NoSuchElementException.class, () -> service.execute(999L, 100L));

        assertEquals("회원을 찾을 수 없습니다.", exception.getMessage());
    }

    @Test
    void 존재하지_않는_상품을_제거해도_예외가_발생하지_않는다() {
        Member member = memberRepository.save(new Member("test@test.com", "password"));

        service.execute(member.getId(), 999L);

        assertTrue(memberRepository.findById(member.getId()).orElseThrow().getWishes().isEmpty());
    }
}
