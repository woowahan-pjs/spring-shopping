package shopping.product.controller

import shopping.product.application.CreateProductRequest

class CreateProductRequestFixture(
    private val name: String = "[p_1] 상품",
    private val price: Int = 1000,
    private val imageUrl: String = "http://localhost:8080/image1",
    private val stockQuantity: Int = 10,
) {
    fun build(): CreateProductRequest =
        CreateProductRequest(
            name = name,
            price = price,
            imageUrl = imageUrl,
            stockQuantity = stockQuantity,
        )
}
