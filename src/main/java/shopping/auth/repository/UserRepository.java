package shopping.auth.repository;

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
}
