package shopping.domain.repository;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import shopping.domain.member.Member;

import java.util.Optional;

public interface MemberRepository {
    boolean existByEmail(@NotBlank @Email String email);

    Member save(Member member);

    Optional<Member> findById(Long id);

    Optional<Member> findByEmail(@NotBlank @Email String email);
}

