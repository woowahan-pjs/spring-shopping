package shopping.wish;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

import shopping.member.MemberRepository;

@Transactional
public class FindWishService implements FindWish {

    private final MemberRepository memberRepository;

    public FindWishService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public List<Wish> execute(UUID memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("회원을 찾을 수 없습니다.")).getWishes();
    }
}
