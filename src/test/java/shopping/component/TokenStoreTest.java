package shopping.component;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;

class TokenStoreTest {

    private TokenStore tokenStore;

    @BeforeEach
    void setUp() {
        tokenStore = new TokenStore();
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
    void findMemberId_만료된_토큰이면_null_반환() throws Exception {
        Long memberId = 1L;
        String token = insertExpiredToken(memberId);

        Long result = tokenStore.findMemberId(token);

        assertThat(result).isNull();
    }

    @Test
    void findMemberId_만료된_토큰은_store에서_제거됨() throws Exception {
        Long memberId = 1L;
        String token = insertExpiredToken(memberId);

        tokenStore.findMemberId(token);

        Long result = tokenStore.findMemberId(token);
        assertThat(result).isNull();
    }

    @Test
    void findMemberId_만료_5분_전_토큰은_갱신됨() throws Exception {
        Long memberId = 1L;
        String token = insertTokenExpiringIn(memberId, 299); // 4분 59초 후 만료

        tokenStore.findMemberId(token);

        Instant renewedExpiresAt = getExpiresAt(token);
        assertThat(renewedExpiresAt).isAfter(Instant.now().plusSeconds(3500)); // 1시간으로 갱신됐는지 확인
    }

    @Test
    void findMemberId_만료_5분_이상_남은_토큰은_정상_통과() throws Exception {
        Long memberId = 1L;
        String token = insertTokenExpiringIn(memberId, 301); // 5분 1초 후 만료

        Long result = tokenStore.findMemberId(token);

        assertThat(result).isEqualTo(memberId);
    }

    // 만료된 TokenEntry를 store에 직접 주입
    @SuppressWarnings("unchecked")
    private String insertExpiredToken(Long memberId) throws Exception {
        return insertTokenExpiringIn(memberId, -1);
    }

    @SuppressWarnings("unchecked")
    private String insertTokenExpiringIn(Long memberId, long secondsUntilExpiry) throws Exception {
        Class<?> entryClass = Class.forName("shopping.component.TokenStore$TokenEntry");
        var constructor = entryClass.getDeclaredConstructor(Long.class, Instant.class);
        constructor.setAccessible(true);
        Object entry = constructor.newInstance(memberId, Instant.now().plusSeconds(secondsUntilExpiry));

        Field storeField = TokenStore.class.getDeclaredField("store");
        storeField.setAccessible(true);
        Map<String, Object> store = (Map<String, Object>) storeField.get(tokenStore);

        String token = "test-token-" + secondsUntilExpiry;
        store.put(token, entry);
        return token;
    }

    @SuppressWarnings("unchecked")
    private Instant getExpiresAt(String token) throws Exception {
        Field storeField = TokenStore.class.getDeclaredField("store");
        storeField.setAccessible(true);
        Map<String, Object> store = (Map<String, Object>) storeField.get(tokenStore);

        Object entry = store.get(token);
        var expiresAtMethod = entry.getClass().getDeclaredMethod("expiresAt");
        expiresAtMethod.setAccessible(true);
        return (Instant) expiresAtMethod.invoke(entry);
    }
}
