package shopping.shop.infrastructure.pesistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopEntityRepository extends JpaRepository<ShopEntity, Long> {
}
