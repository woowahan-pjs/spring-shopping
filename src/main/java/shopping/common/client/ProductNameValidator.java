package shopping.common.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class ProductNameValidator {
    private static final int MAX_NAME_LENGTH = 15;
    private static final Pattern ALLOWED_NAME_PATTERN =
        Pattern.compile("^[a-zA-Z0-9가-힣 ()\\[\\]+\\-&/_]*$");

    private final ProfanityClient profanityClient;

    public void validate(String name) {
        if (name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("상품명은 15자 이하이어야 합니다.");
        }
        if (!ALLOWED_NAME_PATTERN.matcher(name).matches()) {
            throw new IllegalArgumentException("상품명에 허용되지 않은 특수문자가 포함되어 있습니다.");
        }
        if (profanityClient.containsProfanity(name)) {
            throw new IllegalArgumentException("상품명에 비속어가 포함되어 있습니다.");
        }
    }
}
