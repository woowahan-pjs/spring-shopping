package shopping.product.domain;

import java.util.regex.Pattern;
import shopping.common.ApiException;
import shopping.common.ErrorCode;

public record ProductName(String value) {
    private static final Pattern ALLOWED_PATTERN =
            Pattern.compile("^[\\p{L}\\p{N}\\s()\\[\\]+&/_-]+$");

    public ProductName {
        if (value == null || value.isBlank()) {
            throw new ApiException(ErrorCode.INVALID_INPUT);
        }
        if (value.length() > 15) {
            throw new ApiException(ErrorCode.PRODUCT_NAME_TOO_LONG);
        }
        if (!ALLOWED_PATTERN.matcher(value).matches()) {
            throw new ApiException(ErrorCode.PRODUCT_NAME_DISALLOWED_SPECIAL_CHARACTERS);
        }
    }
}
