package shopping.product.domain;

import org.springframework.util.StringUtils;
import shopping.product.exception.InvalidProductException;

import java.util.Objects;
import java.util.regex.Pattern;

public class ProductName {
    public static final int MIN_LENGTH = 15;
    private static final Pattern ALLOWED_SPECIAL_CHARACTERS_PATTERN = Pattern.compile("^[a-zA-Z0-9ㄱ-ㅎ가-힣 ()\\[\\]+\\-&/_]*$");

    private final String name;

    public ProductName(final String name) {
        validate(name);
        this.name = name;
    }

    private void validate(final String name) {
        if (!StringUtils.hasText(name)) {
            throw new InvalidProductException("상품 이름은 필수값 입니다.");
        }

        // 이름 길이 검증
        if (name.length() > MIN_LENGTH) {
            throw new InvalidProductException("상품 이름은 15자 이하여야 합니다.");
        }

        // 특수 문자 검증
        if (!ALLOWED_SPECIAL_CHARACTERS_PATTERN.matcher(name).matches()) {
            throw new InvalidProductException("상품 이름에 허용되지 않는 특수 문자가 포함되어 있습니다.");
        }

//        // 비속어 필터링 (PurgoMalum API 사용)
//        if (containsProfanity(name)) {
//            throw new ProductCreateException("상품 이름에 비속어가 포함될 수 없습니다.");
//        }
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ProductName that = (ProductName) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
