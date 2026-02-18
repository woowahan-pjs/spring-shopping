package shopping.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Component
public class KakaoLoginClient {
    private final KakaoLoginProperties properties;
    private final RestClient restClient;

    public KakaoLoginClient(KakaoLoginProperties properties, RestClient.Builder builder) {
        this.properties = properties;
        this.restClient = builder.build();
    }

    public KakaoTokenResponse requestAccessToken(String code) {
        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", properties.clientId());
        params.add("redirect_uri", properties.redirectUri());
        params.add("code", code);
        params.add("client_secret", properties.clientSecret());

        return restClient.post()
            .uri("https://kauth.kakao.com/oauth/token")
            .header("Content-Type", "application/x-www-form-urlencoded")
            .body(params)
            .retrieve()
            .body(KakaoTokenResponse.class);
    }

    public KakaoUserResponse requestUserInfo(String accessToken) {
        return restClient.get()
            .uri("https://kapi.kakao.com/v2/user/me")
            .header("Authorization", "Bearer " + accessToken)
            .retrieve()
            .body(KakaoUserResponse.class);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record KakaoTokenResponse(@JsonProperty("access_token") String accessToken) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record KakaoUserResponse(@JsonProperty("kakao_account") KakaoAccount kakaoAccount) {

        public String email() {
            return kakaoAccount.email();
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public record KakaoAccount(String email) {
        }
    }
}
