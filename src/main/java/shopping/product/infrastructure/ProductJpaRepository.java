package shopping.product.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {

    @Modifying(clearAutomatically = true)
    @Query("UPDATE ProductEntity p SET p.deletedAt = CURRENT_TIMESTAMP WHERE p.id = :id")
    void deleteById(@Param("id") Long id);
}
