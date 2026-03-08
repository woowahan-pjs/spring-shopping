package shopping.product.repository;

import java.util.Optional;

import jakarta.persistence.QueryHint;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import shopping.product.domain.Price;
import shopping.product.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @QueryHints(
            @QueryHint(
                    name = "org.hibernate.comment",
                    value = "ProductRepository.findProductByIdAndIsUse : 활성화 되어 있는 상품을 조회합니다."))
    Optional<Product> findProductByIdAndIsUse(final Long id, final Boolean isUse);

    @QueryHints(
            @QueryHint(
                    name = "org.hibernate.comment",
                    value =
                            "ProductRepository.findProductByIdAndUserIdAndIsUse : 특정 회원의 활성화 되어 있는 상품을 조회합니다."))
    Optional<Product> findProductByIdAndUserIdAndIsUse(
            final Long id, final Long userId, final Boolean isUse);

    @Query(
            value =
                    """
                SELECT DISTINCT p
                FROM Product p
                WHERE (:name IS NULL OR p.name = :name)
                AND (:fromPrice IS NULL OR p.price >= :fromPrice)
                AND (:toPrice IS NULL OR p.price <= :toPrice)
                AND p.isUse = true
            """,
            countQuery = "SELECT count(p) FROM Product p")
    @QueryHints(
            @QueryHint(
                    name = "org.hibernate.comment",
                    value = "ProductRepository.search : 등록된 상품을 검색합니다."))
    Page<Product> search(
            @Param("name") String name,
            @Param("fromPrice") Price fromPrice,
            @Param("toPrice") Price toPrice,
            Pageable pageable);
}
