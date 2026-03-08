package shopping.component;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenStore {

	private record TokenEntry(Long memberId, Instant expiresAt) {}

	private final Map<String, TokenEntry> store = new ConcurrentHashMap<>();
	private final Duration tokenTtl = Duration.ofHours(1);

	public String save(Long memberId) {
		String token = UUID.randomUUID().toString();
		store.put(token, new TokenEntry(memberId, Instant.now().plus(tokenTtl)));
		return token;
	}

	public Long findMemberId(String token) {
		if (token == null) {
			return null;
		}
		TokenEntry entry = store.get(token);
		if (entry == null || Instant.now().isAfter(entry.expiresAt())) {
			return null;
		}
		if (Instant.now().isAfter(entry.expiresAt().minusSeconds(300))) {
			store.put(token, new TokenEntry(entry.memberId, Instant.now().plus(tokenTtl)));
		}
		return entry.memberId();
	}
}