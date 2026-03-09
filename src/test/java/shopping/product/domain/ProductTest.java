package shopping.product.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shopping.common.ApiException;
import shopping.common.ErrorCode;

@DisplayName("[상품] 상품 도메인 단위 테스트")
class ProductTest {
    @Test
    @DisplayName("상품을 만들면 기본 재고와 활성 상태로 시작한다")
    void createInitializeDefaults() {
        Product product = createProduct(1L, "설명");

        assertThat(product.getName()).isEqualTo("상품");
        assertThat(product.getDescription()).isEqualTo("설명");
        assertThat(product.getPrice()).isEqualTo(new BigDecimal("10000"));
        assertThat(product.getImageUrl()).isEqualTo("https://example.com/image.png");
        assertThat(product.getStockQuantity()).isEqualTo(0);
        assertThat(product.getStatus()).isEqualTo(ProductStatus.ACTIVE);
        assertThat(product.isActive()).isTrue();
    }

    @Test
    @DisplayName("상품을 수정하면 값과 수정자를 갱신한다")
    void updateChangeFields() {
        Product product = createProduct(1L, "설명");

        product.update(
                new ProductName("새상품"),
                "새설명",
                new ProductImageUrl("https://example.com/new-image.png"),
                new ProductPrice(new BigDecimal("20000")),
                1L
        );

        assertThat(product.getName()).isEqualTo("새상품");
        assertThat(product.getDescription()).isEqualTo("새설명");
        assertThat(product.getPrice()).isEqualTo(new BigDecimal("20000"));
        assertThat(product.getImageUrl()).isEqualTo("https://example.com/new-image.png");
        assertThat(product.getUpdatedMemberId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("상품을 삭제하면 삭제 상태와 삭제자를 기록한다")
    void deleteMarkProductDeleted() {
        Product product = createProduct(1L, "설명");

        product.delete(1L);

        assertThat(product.getStatus()).isEqualTo(ProductStatus.DELETED);
        assertThat(product.getDeletedMemberId()).isEqualTo(1L);
        assertThat(product.getDeletedAt()).isNotNull();
        assertThat(product.isActive()).isFalse();
    }

    @Test
    @DisplayName("상품 주인이 아니면 수정하지 못한다")
    void updateRejectWhenMemberIsNotOwner() {
        Product product = createProduct(1L, "설명");

        assertThatThrownBy(() -> product.update(
                new ProductName("새상품"),
                "새설명",
                new ProductImageUrl("https://example.com/new-image.png"),
                new ProductPrice(new BigDecimal("20000")),
                2L
        ))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.PRODUCT_OWNER_FORBIDDEN);
    }

    @Test
    @DisplayName("상품 주인이 아니면 삭제하지 못한다")
    void deleteRejectWhenMemberIsNotOwner() {
        Product product = createProduct(1L, "설명");

        assertThatThrownBy(() -> product.delete(2L))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.PRODUCT_OWNER_FORBIDDEN);
    }

    @Test
    @DisplayName("상품을 만들 때 공백 설명은 null로 저장한다")
    void createNormalizeBlankDescriptionToNull() {
        Product product = createProduct(1L, "   ");

        assertThat(product.getDescription()).isNull();
    }

    @Test
    @DisplayName("상품을 수정할 때 공백 설명은 null로 저장한다")
    void updateNormalizeBlankDescriptionToNull() {
        Product product = createProduct(1L, "설명");

        product.update(
                new ProductName("새상품"),
                "   ",
                new ProductImageUrl("https://example.com/new-image.png"),
                new ProductPrice(new BigDecimal("20000")),
                1L
        );

        assertThat(product.getDescription()).isNull();
    }

    private Product createProduct(Long memberId, String description) {
        return Product.create(
                new ProductName("상품"),
                description,
                new ProductImageUrl("https://example.com/image.png"),
                new ProductPrice(new BigDecimal("10000")),
                memberId
        );
    }
}
