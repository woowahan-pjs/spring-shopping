package shopping.common;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ApiExceptionTest {
    @Test
    @DisplayName("ApiException은 상태 코드와 에러 코드를 그대로 노출한다")
    void exposeStatusAndCode() {
        ApiException exception = new ApiException(ErrorCode.PRODUCT_NOT_FOUND, "상품이 없다");

        assertThat(exception.getStatus()).isEqualTo(ErrorCode.PRODUCT_NOT_FOUND.status());
        assertThat(exception.getCode()).isEqualTo(ErrorCode.PRODUCT_NOT_FOUND.code());
        assertThat(exception.getMessage()).isEqualTo("상품이 없다");
    }
}
