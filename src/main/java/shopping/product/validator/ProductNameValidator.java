package shopping.product.validator;

import shopping.common.exception.InvalidProductNameException;

import java.util.regex.Pattern;

public class ProductNameValidator {

    private static final Pattern PATTERN = Pattern.compile("^[a-zA-Z0-9가-힣()\\[\\]+\\-&/_ ]+$");

    public void validate(String name) {
        if (!PATTERN.matcher(name).matches()) {
            throw new InvalidProductNameException("상품 이름이 올바르지 않습니다. (), [], +, -, &, /, _ 특수문자만 사용 가능합니다.");
        }
    }
}
