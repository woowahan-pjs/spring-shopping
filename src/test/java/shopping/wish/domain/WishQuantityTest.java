package shopping.wish.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import shopping.common.ApiException;
import shopping.common.ErrorCode;

@DisplayName("[위시] 위시 수량 값 객체 단위 테스트")
class WishQuantityTest {
    @Test
    @DisplayName("수량이 없으면 기본값 1을 사용한다")
    void fromUseDefaultQuantity() {
        WishQuantity wishQuantity = WishQuantity.from(null);

        assertThat(wishQuantity.value()).isEqualTo(1);
    }

    @ParameterizedTest(name = "[{index}] 수량={0}")
    @ValueSource(ints = {1, 2, 10})
    @DisplayName("양수 수량은 그대로 사용한다")
    void fromUseRequestedQuantity(int quantity) {
        WishQuantity wishQuantity = WishQuantity.from(quantity);

        assertThat(wishQuantity.value()).isEqualTo(quantity);
    }

    @ParameterizedTest(name = "[{index}] 수량={0}")
    @ValueSource(ints = {0, -1})
    @DisplayName("0 이하 수량은 허용하지 않는다")
    void fromRejectNonPositiveQuantity(int quantity) {
        assertThatThrownBy(() -> WishQuantity.from(quantity))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.WISH_QUANTITY_INVALID);
    }
}
