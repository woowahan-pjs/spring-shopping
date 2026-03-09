package shopping.common;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "INVALID_INPUT", "Request input is invalid."),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "Unexpected server error."),
    MEMBER_EMAIL_DUPLICATE(HttpStatus.CONFLICT, "MEMBER_EMAIL_DUPLICATE", "Email is already registered."),
    MEMBER_CREDENTIALS_INVALID(HttpStatus.UNAUTHORIZED, "MEMBER_CREDENTIALS_INVALID", "Invalid email or password."),
    MEMBER_INACTIVE_FORBIDDEN(HttpStatus.FORBIDDEN, "MEMBER_INACTIVE_FORBIDDEN", "Inactive member cannot access this resource."),
    MEMBER_SELLER_REQUIRED(HttpStatus.FORBIDDEN, "MEMBER_SELLER_REQUIRED", "Seller role is required."),
    AUTHORIZATION_HEADER_REQUIRED(HttpStatus.UNAUTHORIZED, "AUTH_HEADER_REQUIRED", "Authorization header is required."),
    AUTHORIZATION_HEADER_INVALID(HttpStatus.UNAUTHORIZED, "AUTH_HEADER_INVALID", "Authorization header must use Bearer token."),
    AUTHENTICATION_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "AUTH_TOKEN_INVALID", "Authentication token is invalid."),
    REFRESH_TOKEN_REQUIRED(HttpStatus.UNAUTHORIZED, "REFRESH_TOKEN_REQUIRED", "Refresh token is required."),
    REFRESH_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "REFRESH_TOKEN_INVALID", "Refresh token is invalid."),
    WISHLIST_NOT_FOUND(HttpStatus.NOT_FOUND, "WISHLIST_NOT_FOUND", "Wishlist not found."),
    WISH_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "WISH_ITEM_NOT_FOUND", "Wish item not found."),
    WISH_ALREADY_EXISTS(HttpStatus.CONFLICT, "WISH_ALREADY_EXISTS", "Wish already exists for this product."),
    WISH_QUANTITY_INVALID(HttpStatus.BAD_REQUEST, "WISH_QUANTITY_INVALID", "Wish quantity must be greater than 0."),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "PRODUCT_NOT_FOUND", "Product not found."),
    PRODUCT_OWNER_FORBIDDEN(HttpStatus.FORBIDDEN, "PRODUCT_OWNER_FORBIDDEN", "Only product owner can modify this product."),
    PRODUCT_NAME_TOO_LONG(HttpStatus.BAD_REQUEST, "PRODUCT_NAME_TOO_LONG", "Product name must be at most 15 characters."),
    PRODUCT_NAME_DISALLOWED_SPECIAL_CHARACTERS(HttpStatus.BAD_REQUEST, "PRODUCT_NAME_DISALLOWED_SPECIAL_CHARACTERS", "Product name contains disallowed special characters."),
    PRODUCT_PRICE_INVALID(HttpStatus.BAD_REQUEST, "PRODUCT_PRICE_INVALID", "Product price must be greater than 0."),
    PRODUCT_IMAGE_URL_NOT_ABSOLUTE(HttpStatus.BAD_REQUEST, "PRODUCT_IMAGE_URL_NOT_ABSOLUTE", "Image URL must be absolute."),
    PRODUCT_IMAGE_URL_INVALID_FORMAT(HttpStatus.BAD_REQUEST, "PRODUCT_IMAGE_URL_INVALID_FORMAT", "Image URL format is invalid."),
    PRODUCT_NAME_CONTAINS_SLANG(HttpStatus.BAD_REQUEST, "PRODUCT_NAME_CONTAINS_SLANG", "Product name must not contain slang."),
    SLANG_VERIFY_FAILED(HttpStatus.SERVICE_UNAVAILABLE, "SLANG_VERIFY_FAILED", "Failed to verify slang."),
    SLANG_API_CALL_FAILED(HttpStatus.SERVICE_UNAVAILABLE, "SLANG_API_CALL_FAILED", "Failed to call slang API."),
    SLANG_API_INTERRUPTED(HttpStatus.SERVICE_UNAVAILABLE, "SLANG_API_INTERRUPTED", "Slang API call was interrupted."),
    SLANG_API_INVALID_RESPONSE(HttpStatus.SERVICE_UNAVAILABLE, "SLANG_API_INVALID_RESPONSE", "Slang API response is invalid.");

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
