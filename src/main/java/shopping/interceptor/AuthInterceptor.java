package shopping.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import shopping.component.MemberContext;
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
		Long memberId = tokenStore.findMemberId(token);
		if (token == null || memberId == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return false;
		}
		if (memberId == -1L) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType("application/json");
			response.getWriter().write("{\"error\": \"토큰 유효시간이 만료되었습니다. 다시 로그인 해주세요\"}");
			return false;
		}
		MemberContext.setMemberId(memberId);
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		MemberContext.clear();
	}
}