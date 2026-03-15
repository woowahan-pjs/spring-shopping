package shopping.component;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

class TokenStoreTest {

    private AdjustableClock clock;
    private TokenStore tokenStore;

    @BeforeEach
    void setUp() {
        clock = new AdjustableClock(Instant.now());
        tokenStore = new TokenStore(clock);
    }

    @Test
    void findMemberId_토큰이_null이면_null_반환() {
        Long result = tokenStore.findMemberId(null);

        assertThat(result).isNull();
    }

    @Test
    void findMemberId_존재하지_않는_토큰이면_null_반환() {
        Long result = tokenStore.findMemberId("invalid-token");

        assertThat(result).isNull();
    }

    @Test
    void save_후_findMemberId_정상_반환() {
        Long memberId = 1L;
        String token = tokenStore.save(memberId);

        Long result = tokenStore.findMemberId(token);

        assertThat(result).isEqualTo(memberId);
    }

    @Test
    void findMemberId_만료된_토큰이면_null_반환() {
        Long memberId = 1L;
        String token = tokenStore.save(memberId);
        clock.advance(Duration.ofHours(1).plusSeconds(1));

        Long result = tokenStore.findMemberId(token);

        assertThat(result).isNull();
    }

    @Test
    void findMemberId_만료된_토큰은_store에서_제거됨() {
        Long memberId = 1L;
        String token = tokenStore.save(memberId);
        clock.advance(Duration.ofHours(1).plusSeconds(1));

        tokenStore.findMemberId(token);
        Long result = tokenStore.findMemberId(token);

        assertThat(result).isNull();
    }

    @Test
    void findMemberId_만료_5분_전_토큰은_갱신됨() {
        Long memberId = 1L;
        String token = tokenStore.save(memberId); // 만료: 현재 + 1시간
        clock.advance(Duration.ofSeconds(3301));  // 만료까지 4분 59초 남음

        tokenStore.findMemberId(token); // 갱신 발생

        clock.advance(Duration.ofSeconds(300));   // 원래 만료 시각 경과
        Long result = tokenStore.findMemberId(token);
        assertThat(result).isEqualTo(memberId);  // 갱신된 토큰은 여전히 유효
    }

    @Test
    void findMemberId_만료_5분_이상_남은_토큰은_정상_통과() {
        Long memberId = 1L;
        String token = tokenStore.save(memberId); // 만료: 현재 + 1시간
        clock.advance(Duration.ofSeconds(3299));  // 만료까지 5분 1초 남음

        Long result = tokenStore.findMemberId(token);

        assertThat(result).isEqualTo(memberId);
    }

    static class AdjustableClock extends Clock {

        private Instant instant;

        AdjustableClock(Instant instant) {
            this.instant = instant;
        }

        void advance(Duration duration) {
            this.instant = this.instant.plus(duration);
        }

        @Override
        public ZoneOffset getZone() {
            return ZoneOffset.UTC;
        }

        @Override
        public Clock withZone(ZoneId zone) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Instant instant() {
            return instant;
        }
    }
}