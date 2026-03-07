package shopping.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import shopping.component.TokenStore;

@Component
public class AuthInterceptor implements HandlerInterceptor {

	private final TokenStore tokenStore;

	public AuthInterceptor(TokenStore tokenStore) {
		this.tokenStore = tokenStore;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String token = request.getHeader("Authorization");
		if (token == null || tokenStore.findMemberId(token) == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return false;
		}
		return true;
	}
}