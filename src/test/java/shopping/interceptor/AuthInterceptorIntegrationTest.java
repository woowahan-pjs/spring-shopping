package shopping.interceptor;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import shopping.component.ProductNameValidator;
import shopping.component.TokenStore;

@SpringBootTest
@AutoConfigureMockMvc
class AuthInterceptorIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private TokenStore tokenStore;

	private String validToken;

	@BeforeEach
	void setUp() {
		validToken = tokenStore.save(1L);
	}

	@Test
	void 유효한_토큰으로_요청시_200_반환() throws Exception {
		mockMvc.perform(get("/api/product/list")
				.header("Authorization", validToken))
			.andExpect(status().isOk());
	}

	@Test
	void 토큰_없이_요청시_401_반환() throws Exception {
		mockMvc.perform(get("/api/product/list"))
			.andExpect(status().isUnauthorized());
	}

	@Test
	void 유효하지_않은_토큰으로_요청시_401_반환() throws Exception {
		mockMvc.perform(get("/api/product/list")
				.header("Authorization", "invalid-token"))
			.andExpect(status().isUnauthorized());
	}

	@Test
	void 로그인_요청은_토큰_없이_접근_가능() throws Exception {
		mockMvc.perform(get("/api/member/login"))
			.andExpect(status().isMethodNotAllowed()); // 405: GET이지만 401은 아님 (인증 제외 경로)
	}
}