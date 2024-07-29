package shopping.product.application

import shopping.common.error.ApiException
import shopping.common.error.ErrorCode

class ProductNotFoundException(
    productId: Long,
) : ApiException(errorCode = ErrorCode.PRODUCT_NOT_FOUND, message = "상품 ID: ${productId}을 찾을 수 없습니다.")
