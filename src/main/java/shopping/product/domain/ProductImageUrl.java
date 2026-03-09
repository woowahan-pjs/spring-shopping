package shopping.product.domain;

import java.net.URI;
import java.net.URISyntaxException;
import shopping.common.ApiException;
import shopping.common.ErrorCode;

public record ProductImageUrl(String value) {
    public ProductImageUrl {
        if (value == null || value.isBlank()) {
            throw new ApiException(ErrorCode.INVALID_INPUT);
        }
        URI uri = toUri(value);
        if (uri.getScheme() == null || uri.getHost() == null) {
            throw new ApiException(ErrorCode.PRODUCT_IMAGE_URL_NOT_ABSOLUTE);
        }
    }

    private URI toUri(String value) {
        try {
            return new URI(value);
        } catch (URISyntaxException exception) {
            throw new ApiException(ErrorCode.PRODUCT_IMAGE_URL_INVALID_FORMAT);
        }
    }
}
