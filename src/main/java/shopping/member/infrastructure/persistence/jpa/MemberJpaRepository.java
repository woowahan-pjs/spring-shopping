package shopping.member.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import shopping.member.domain.MemberEntity;

import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<MemberEntity, Long> {
    Optional<MemberEntity> findByEmail(String email);
}
