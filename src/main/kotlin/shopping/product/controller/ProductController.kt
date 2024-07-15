package shopping.product.controller

import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import shopping.common.api.ApiResponse
import shopping.product.application.*

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

    @PutMapping("/{productId}")
    fun updateProduct(
        @Valid @RequestBody request: UpdateProductRequest,
        @PathVariable(name = "productId") productId: Long,
    ): ApiResponse<UpdateProductResponse> {
        val response = productService.updateProduct(productId = productId, request = request)

        return ApiResponse.success(response)
    }

    @GetMapping("/{productId}")
    fun getProduct(
        @PathVariable(name = "productId") productId: Long,
    ): ApiResponse<ProductDetailResponse> {
        val response = productService.getProduct(productId)

        return ApiResponse.success(response)
    }
}
