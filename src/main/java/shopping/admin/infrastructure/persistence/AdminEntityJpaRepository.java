package shopping.admin.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminEntityJpaRepository extends JpaRepository<AdminEntity, Long> {
    Optional<AdminEntity> findByEmail(final String email);
}
