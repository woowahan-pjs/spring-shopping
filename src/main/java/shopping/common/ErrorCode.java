package shopping.common;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "INVALID_INPUT", "Request input is invalid."),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "Unexpected server error."),
    MEMBER_EMAIL_DUPLICATE(HttpStatus.CONFLICT, "MEMBER_CONFLICT", "Email is already registered."),
    MEMBER_CREDENTIALS_INVALID(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "Invalid email or password."),
    MEMBER_INACTIVE_FORBIDDEN(HttpStatus.FORBIDDEN, "MEMBER_INACTIVE", "Inactive member cannot access this resource."),
    MEMBER_SELLER_REQUIRED(HttpStatus.FORBIDDEN, "SELLER_REQUIRED", "Seller role is required."),
    AUTHORIZATION_HEADER_REQUIRED(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "Authorization header is required."),
    AUTHORIZATION_HEADER_INVALID(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "Authorization header must use Bearer token."),
    AUTHENTICATION_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "Authentication token is invalid."),
    WISHLIST_NOT_FOUND(HttpStatus.NOT_FOUND, "WISH_NOT_FOUND", "Wishlist not found."),
    WISH_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "WISH_NOT_FOUND", "Wish item not found."),
    WISH_ALREADY_EXISTS(HttpStatus.CONFLICT, "WISH_CONFLICT", "Wish already exists for this product."),
    WISH_QUANTITY_INVALID(HttpStatus.BAD_REQUEST, "INVALID_WISH_INPUT", "Wish quantity must be greater than 0."),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "PRODUCT_NOT_FOUND", "Product not found."),
    PRODUCT_OWNER_FORBIDDEN(HttpStatus.FORBIDDEN, "PRODUCT_OWNER_FORBIDDEN", "Only product owner can modify this product."),
    PRODUCT_NAME_TOO_LONG(HttpStatus.BAD_REQUEST, "INVALID_PRODUCT_INPUT", "Product name must be at most 15 characters."),
    PRODUCT_NAME_DISALLOWED_SPECIAL_CHARACTERS(
            HttpStatus.BAD_REQUEST,
            "INVALID_PRODUCT_INPUT",
            "Product name contains disallowed special characters."
    ),
    PRODUCT_PRICE_INVALID(HttpStatus.BAD_REQUEST, "INVALID_PRODUCT_INPUT", "Product price must be greater than 0."),
    PRODUCT_IMAGE_URL_NOT_ABSOLUTE(HttpStatus.BAD_REQUEST, "INVALID_PRODUCT_INPUT", "Image URL must be absolute."),
    PRODUCT_IMAGE_URL_INVALID_FORMAT(HttpStatus.BAD_REQUEST, "INVALID_PRODUCT_INPUT", "Image URL format is invalid."),
    PRODUCT_NAME_CONTAINS_PROFANITY(HttpStatus.BAD_REQUEST, "INVALID_PRODUCT_INPUT", "Product name must not contain profanity."),
    PROFANITY_VERIFY_FAILED(HttpStatus.SERVICE_UNAVAILABLE, "PROFANITY_API_ERROR", "Failed to verify profanity."),
    PROFANITY_API_CALL_FAILED(HttpStatus.SERVICE_UNAVAILABLE, "PROFANITY_API_ERROR", "Failed to call profanity API."),
    PROFANITY_API_INTERRUPTED(HttpStatus.SERVICE_UNAVAILABLE, "PROFANITY_API_ERROR", "Profanity API call was interrupted."),
    PROFANITY_API_INVALID_RESPONSE(HttpStatus.SERVICE_UNAVAILABLE, "PROFANITY_API_ERROR", "Profanity API response is invalid.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public HttpStatus status() {
        return status;
    }

    public String code() {
        return code;
    }

    public String message() {
        return message;
    }
}
