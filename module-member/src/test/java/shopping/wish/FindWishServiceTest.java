package shopping.wish;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import shopping.member.InMemoryMemberRepository;
import shopping.member.Member;

class FindWishServiceTest {

    private InMemoryMemberRepository memberRepository;
    private FindWishService service;

    @BeforeEach
    void setUp() {
        memberRepository = new InMemoryMemberRepository();
        service = new FindWishService(memberRepository);
    }

    @Test
    void 위시리스트를_조회한다() {
        Member member = memberRepository.save(new Member("test@test.com", "password"));
        member.wish(100L);
        member.wish(200L);
        memberRepository.save(member);

        List<Wish> wishes = service.execute(member.getId());

        assertEquals(2, wishes.size());
    }

    @Test
    void 위시리스트가_비어있으면_빈_리스트를_반환한다() {
        Member member = memberRepository.save(new Member("test@test.com", "password"));

        List<Wish> wishes = service.execute(member.getId());

        assertTrue(wishes.isEmpty());
    }

    @Test
    void 존재하지_않는_회원이면_예외가_발생한다() {
        NoSuchElementException exception =
                assertThrows(NoSuchElementException.class, () -> service.execute(999L));

        assertEquals("회원을 찾을 수 없습니다.", exception.getMessage());
    }
}
