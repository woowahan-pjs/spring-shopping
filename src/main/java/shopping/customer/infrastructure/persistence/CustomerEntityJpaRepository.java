package shopping.customer.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerEntityJpaRepository extends JpaRepository<CustomerEntity, Long> {
    CustomerEntity findByEmail(String email);
}
