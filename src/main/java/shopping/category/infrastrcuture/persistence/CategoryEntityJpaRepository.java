package shopping.category.infrastrcuture.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryEntityJpaRepository extends JpaRepository<MainCategoryEntity, Long> {
}
