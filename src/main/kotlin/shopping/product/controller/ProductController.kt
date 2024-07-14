package shopping.product.controller

import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import shopping.common.api.ApiResponse
import shopping.product.application.CreateProductRequest
import shopping.product.application.CreateProductResponse
import shopping.product.application.ProductService

@RestController
@RequestMapping("/products")
class ProductController(
    private val productService: ProductService,
) {
    @PostMapping
    fun createProduct(
        @Valid @RequestBody request: CreateProductRequest,
    ): ApiResponse<CreateProductResponse> {
        val response = productService.createProduct(request)

        return ApiResponse.success(response)
    }
}
