package shopping.product.presentation.dto.response

import shopping.product.domain.Product

class ProductRegisterResponse(product: Product) {
    val id: Long = product.id
}
