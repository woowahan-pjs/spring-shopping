package shopping.product.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Getter;
import shopping.product.exception.InvalidProductNameException;

@Embeddable
public class ProductName {

    private static final Pattern PRODUCT_NAME_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9()\\[\\]+\\-&/_가-힣]*$");


    @Column(name = "product_name", nullable = false)
    @Getter
    private String name;

    protected ProductName() {
    }

    public ProductName(final String name, final ProfanityChecker profanityChecker) {
        if (name == null || profanityChecker == null) {
            throw new InvalidProductNameException("상품 이름은 필수값 입니다");
        }

        validateName(name, profanityChecker);
        this.name = name;
    }

    private static void validateName(final String name, final ProfanityChecker profanityChecker) {
        validateNameLength(name);
        validateNamePattern(name);
        validateProfanity(name, profanityChecker);
    }

    private static void validateNameLength(final String name) {
        if (name.length() > 15) {
            throw new InvalidProductNameException("상품 이름은 최대 15글자까지 입력가능합니다. " + name);
        }
    }

    private static void validateNamePattern(final String name) {
        final Matcher nameMatcher = PRODUCT_NAME_PATTERN.matcher(name);
        if (!nameMatcher.matches()) {
            throw new InvalidProductNameException(
                    "상품 이름에 (), [], +, -, &, /, _ 을 제외한 특수문자는 들어갈 수 없습니다. " + name);
        }
    }

    private static void validateProfanity(final String name,
            final ProfanityChecker profanityChecker) {
        if (profanityChecker.isProfanity(name)) {
            throw new InvalidProductNameException("상품 이름에 비속어를 사용할 수 없습니다. " + name);
        }
    }
}
