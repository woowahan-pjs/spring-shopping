package shopping.api.presentation.v1

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import shopping.api.presentation.v1.dto.CreateProductRequest
import shopping.api.presentation.v1.dto.ProductResponse
import shopping.api.presentation.v1.dto.UpdateProductRequest
import shopping.api.presentation.v1.validator.ProductValidator
import shopping.application.ProductService
import shopping.support.response.ApiResponse

@RestController
@RequestMapping("/api/v1/products")
class ProductController(
    private val productService: ProductService,
    private val productValidator: ProductValidator,
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @RequestBody request: CreateProductRequest,
    ): ApiResponse<ProductResponse> {
        productValidator.validateCreate(request)
        val productId = productService.create(request.toCommand())
        val result = productService.getProduct(productId)
        return ApiResponse.success(ProductResponse.from(result))
    }

    @GetMapping("/{id}")
    fun getProduct(
        @PathVariable id: Long,
    ): ApiResponse<ProductResponse> {
        val result = productService.getProduct(id)
        return ApiResponse.success(ProductResponse.from(result))
    }

    @GetMapping
    fun getProducts(): ApiResponse<List<ProductResponse>> {
        val results = productService.getProducts()
        return ApiResponse.success(results.map { ProductResponse.from(it) })
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody request: UpdateProductRequest,
    ): ApiResponse<Any> {
        productValidator.validateUpdate(request)
        productService.update(id, request.toCommand())
        return ApiResponse.success()
    }

    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable id: Long,
    ): ApiResponse<Any> {
        productService.delete(id)
        return ApiResponse.success()
    }
}
