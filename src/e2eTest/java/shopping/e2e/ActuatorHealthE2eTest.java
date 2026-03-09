package shopping.e2e;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import shopping.e2e.support.AbstractE2eTest;

@DisplayName("[공통] Actuator 헬스체크 통합 테스트")
class ActuatorHealthE2eTest extends AbstractE2eTest {
    @Test
    @DisplayName("헬스체크 엔드포인트는 인증 없이 status를 반환한다")
    void healthReturnStatusWithoutAuthentication() throws Exception {
        // given
        HttpHeaders headers = jsonHeaders();

        // when
        ResponseEntity<String> response = get("/actuator/health", headers);

        // then
        assertStatus(response, HttpStatus.OK);
        JsonNode body = objectMapper.readTree(response.getBody());
        assertThat(body.has("status")).isTrue();
        assertThat(body.get("status").asText()).isEqualTo("UP");
    }

    @ParameterizedTest(name = "[{index}] 경로={0}")
    @ValueSource(strings = {
            "/actuator/health/liveness",
            "/actuator/health/readiness"
    })
    @DisplayName("liveness와 readiness 엔드포인트는 공개된다")
    void healthProbeEndpointsArePublic(String path) throws Exception {
        // given
        HttpHeaders headers = jsonHeaders();

        // when
        ResponseEntity<String> response = get(path, headers);

        // then
        assertStatus(response, HttpStatus.OK);
        JsonNode body = objectMapper.readTree(response.getBody());
        assertThat(body.has("status")).isTrue();
        assertThat(body.get("status").asText()).isEqualTo("UP");
    }

    @Test
    @DisplayName("metrics 엔드포인트는 웹으로 노출하지 않는다")
    void metricsIsNotExposedOverHttp() {
        // given
        HttpHeaders headers = jsonHeaders();

        // when
        ResponseEntity<String> response = get("/actuator/metrics", headers);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
