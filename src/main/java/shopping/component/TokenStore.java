package shopping.component;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenStore {

	private final Map<String, Long> store = new ConcurrentHashMap<>();

	public String save(Long memberId) {
		String token = UUID.randomUUID().toString();
		store.put(token, memberId);
		return token;
	}

	public Long findMemberId(String token) {
		if (token == null) {
			return null;
		}
		return store.get(token);
	}
}