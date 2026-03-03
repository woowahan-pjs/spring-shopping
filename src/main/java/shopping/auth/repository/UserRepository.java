package shopping.auth.repository;

import java.util.Optional;

import jakarta.persistence.QueryHint;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;

import shopping.auth.domain.User;

public interface UserRepository extends CrudRepository<User, Long> {

    @QueryHints(
            @QueryHint(
                    name = "org.hibernate.comment",
                    value = "UserRepository.existsByEmail : 특정 이메일이 있는지 여부를 조회합니다."))
    boolean existsByEmail(final String email);

    @QueryHints(
            @QueryHint(
                    name = "org.hibernate.comment",
                    value =
                            "UserRepository.findByEmailAndIsUse : 특정 이메일로 가입 되고 활성화 되어 있는 회원을 조회합니다."))
    Optional<User> findByEmailAndIsUse(final String email, final Boolean isUse);
}
