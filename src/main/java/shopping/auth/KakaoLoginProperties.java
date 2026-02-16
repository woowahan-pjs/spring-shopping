package shopping.auth;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kakao.login")
public record KakaoLoginProperties(String clientId, String clientSecret, String redirectUri) {
}
