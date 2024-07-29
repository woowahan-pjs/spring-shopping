package shopping.product.domain

import shopping.common.error.ApiException
import shopping.common.error.ErrorCode

class ProductNameContainBadWordException(
    name: String,
) : ApiException(
        errorCode = ErrorCode.PRODUCT_NAME_CONTAIN_BAD_WORD,
        message = "상품명에 금지어가 포함되어 있습니다. (상품명: $name)",
    )
