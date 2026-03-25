package shopping.product.adapter.out;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import feign.FeignException;
import feign.Request;
import feign.Response;
import feign.RetryableException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shopping.common.ApiException;
import shopping.common.ErrorCode;

@ExtendWith(MockitoExtension.class)
@DisplayName("[상품] PurgoMalum 영어 비속어 어댑터 단위 테스트")
class PurgomalumEnglishSlangAdapterTest {
    @Mock
    private PurgomalumFeignClient purgomalumFeignClient;

    @ParameterizedTest(name = "[{index}] 응답={0}, 결과={1}")
    @CsvSource({
            "true, true",
            "false, false"
    })
    @DisplayName("PurgoMalum 응답이 true와 false면 그대로 파싱한다")
    void containsSlangParseBooleanBody(String body, boolean expected) {
        PurgomalumEnglishSlangAdapter adapter = new PurgomalumEnglishSlangAdapter(purgomalumFeignClient);
        when(purgomalumFeignClient.containsSlang("sample")).thenReturn(body);

        boolean result = adapter.containsSlang("sample");

        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("응답 본문이 예상 형식이 아니면 예외를 던진다")
    void containsSlangThrowWhenBodyIsInvalid() {
        PurgomalumEnglishSlangAdapter adapter = new PurgomalumEnglishSlangAdapter(purgomalumFeignClient);
        when(purgomalumFeignClient.containsSlang("sample")).thenReturn("maybe");

        assertThatThrownBy(() -> adapter.containsSlang("sample"))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.SLANG_API_INVALID_RESPONSE);
    }

    @Test
    @DisplayName("응답 코드가 2xx가 아니면 예외를 던진다")
    void containsSlangThrowWhenStatusIsNotSuccess() {
        PurgomalumEnglishSlangAdapter adapter = new PurgomalumEnglishSlangAdapter(purgomalumFeignClient);
        when(purgomalumFeignClient.containsSlang("sample")).thenThrow(createFeignException(500, "error"));

        assertThatThrownBy(() -> adapter.containsSlang("sample"))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.SLANG_VERIFY_FAILED);
    }

    @Test
    @DisplayName("호출에 실패하면 API 호출 실패 예외를 던진다")
    void containsSlangThrowWhenCallFails() {
        PurgomalumEnglishSlangAdapter adapter = new PurgomalumEnglishSlangAdapter(purgomalumFeignClient);
        when(purgomalumFeignClient.containsSlang("sample")).thenThrow(createRetryableException());

        assertThatThrownBy(() -> adapter.containsSlang("sample"))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.SLANG_API_CALL_FAILED);
    }

    private FeignException createFeignException(int statusCode, String body) {
        Response response = Response.builder()
                .status(statusCode)
                .reason("error")
                .request(createRequest())
                .headers(Collections.emptyMap())
                .body(body, StandardCharsets.UTF_8)
                .build();
        return FeignException.errorStatus("PurgomalumFeignClient#containsSlang(String)", response);
    }

    private RetryableException createRetryableException() {
        return new RetryableException(
                0,
                "Connection refused",
                Request.HttpMethod.GET,
                new IOException("Connection refused"),
                (Long) null,
                createRequest()
        );
    }

    private Request createRequest() {
        return Request.create(
                Request.HttpMethod.GET,
                "http://localhost/service/containsprofanity?text=sample",
                Collections.emptyMap(),
                null,
                StandardCharsets.UTF_8,
                null
        );
    }
}
