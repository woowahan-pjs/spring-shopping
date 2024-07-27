package shopping.product.presentation.dto.response

import shopping.product.domain.Product
import java.math.BigDecimal

class ProductsResponse(products: List<Product>) {
    val products: List<ProductResponse> = products.map(::ProductResponse)
}
