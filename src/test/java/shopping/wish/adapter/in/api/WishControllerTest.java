package shopping.wish.adapter.in.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import shopping.wish.service.WishService;

@ExtendWith(MockitoExtension.class)
@DisplayName("[위시] 위시 컨트롤러 단위 테스트")
class WishControllerTest {
    @Mock
    private WishService wishService;

    @Test
    @DisplayName("위시를 추가하면 생성 응답을 돌려준다")
    void addReturnCreatedResponse() {
        // given
        WishController controller = new WishController(wishService);
        WishCreateRequest request = new WishCreateRequest(10L, 2);
        WishResponse response = new WishResponse(
                1L,
                10L,
                "상품",
                new BigDecimal("10000"),
                "https://example.com/image.png",
                2,
                LocalDateTime.of(2026, 3, 8, 12, 0)
        );
        when(wishService.add(1L, 10L, 2)).thenReturn(response);

        // when
        ResponseEntity<WishResponse> result = controller.add(1L, request);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody()).isEqualTo(response);
    }

    @Test
    @DisplayName("위시를 삭제하면 204를 돌려준다")
    void deleteReturnNoContent() {
        // given
        WishController controller = new WishController(wishService);

        // when
        ResponseEntity<Void> result = controller.delete(1L, 2L);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(wishService).delete(1L, 2L);
    }

    @Test
    @DisplayName("위시 목록을 조회하면 200과 목록을 돌려준다")
    void listReturnOkResponse() {
        // given
        WishController controller = new WishController(wishService);
        List<WishResponse> response = List.of(
                new WishResponse(
                        1L,
                        10L,
                        "상품",
                        new BigDecimal("10000"),
                        "https://example.com/image.png",
                        2,
                        LocalDateTime.of(2026, 3, 8, 12, 0)
                )
        );
        when(wishService.list(1L)).thenReturn(response);

        // when
        ResponseEntity<List<WishResponse>> result = controller.list(1L);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(response);
    }
}
