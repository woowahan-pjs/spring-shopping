package shopping.component;

import org.springframework.stereotype.Component;

import java.time.Clock;
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
	private final Clock clock;

	public TokenStore() {
		this(Clock.systemUTC());
	}

	TokenStore(Clock clock) {
		this.clock = clock;
	}

	public String save(Long memberId) {
		String token = UUID.randomUUID().toString();
		store.put(token, new TokenEntry(memberId, clock.instant().plus(tokenTtl)));
		return token;
	}

	public Long findMemberId(String token) {
		if (token == null) {
			return null;
		}
		TokenEntry entry = store.get(token);
		if (entry == null || clock.instant().isAfter(entry.expiresAt())) {
			return null;
		}
		Instant beforeExpiresAt = entry.expiresAt().minusSeconds(300);
		boolean needTokenExtension = clock.instant().isAfter(beforeExpiresAt);
		if (needTokenExtension) {
			store.put(token, new TokenEntry(entry.memberId(), clock.instant().plus(tokenTtl)));
		}
		return entry.memberId();
	}
}