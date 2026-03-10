package shopping.common.dto;

import shopping.common.exception.ErrorType;

public record ErrorResponse(String errorMessage, ErrorType errorType) {

}
