package shopping.product.repository;

import java.util.Optional;

import jakarta.persistence.QueryHint;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;

import shopping.product.domain.Product;

public interface ProductRepository extends CrudRepository<Product, Long> {

    @QueryHints(
            @QueryHint(
                    name = "org.hibernate.comment",
                    value = "ProductRepository.findProductByIdAndIsUse : 활성화 되어 있는 상품을 조회합니다."))
    Optional<Product> findProductByIdAndIsUse(final Long id, final Boolean isUse);

    @QueryHints(
        @QueryHint(
            name = "org.hibernate.comment",
            value = "ProductRepository.findProductByIdAndUserIdAndIsUse : 특정 회원의 활성화 되어 있는 상품을 조회합니다."))
    Optional<Product> findProductByIdAndUserIdAndIsUse(final Long id, final Long userId, final Boolean isUse);
}
