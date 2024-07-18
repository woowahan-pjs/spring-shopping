package shopping.seller.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SellerEntityJpaRepository extends JpaRepository<SellerEntity, Long> {
    Optional<SellerEntity> findByEmail(String email);
}
