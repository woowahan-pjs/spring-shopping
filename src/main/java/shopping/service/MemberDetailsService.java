package shopping.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shopping.entity.Member;
import shopping.entity.UserDetail;
import shopping.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberDetailsService {
    private final MemberRepository memberRepository;

    public UserDetail loadUserByEmail(final String email) {
        final Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(email));
        return new UserDetail(member.getId(), member.getEmail(), member.getPassword());
    }
}
