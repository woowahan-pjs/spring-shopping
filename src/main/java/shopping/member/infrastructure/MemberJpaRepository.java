package shopping.member.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<MemberEntity, Long> {

    @Query("SELECT m FROM MemberEntity m WHERE m.email = :email")
    Optional<MemberEntity> findByEmail(@Param("email") String email);
}
