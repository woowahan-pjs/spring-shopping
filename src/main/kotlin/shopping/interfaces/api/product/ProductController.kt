package shopping.interfaces.api.product

import com.failsafe.interfaces.api.ApiResponse
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import shopping.application.product.ProductFacade
import shopping.interfaces.api.Paging

@RestController
@RequestMapping("/api/products")
class ProductController(
    private val productFacade: ProductFacade
) {
    @GetMapping
    fun getProducts(
        @RequestBody request: ProductV1Dto.ProductSearchRequest
    ): ApiResponse<Paging.PageResponse<ProductV1Dto.ViewResponse>> {
        return productFacade.getProducts(request.paging)
            .map { ProductV1Dto.ViewResponse.from(it) }
            .let { Paging.PageResponse.from(it) }
            .let { ApiResponse.success(it) }
    }

    @PostMapping
    fun createProduct(
        @RequestBody request: ProductV1Dto.CreateProductRequest
    ): ApiResponse<ProductV1Dto.ViewResponse> {
        return productFacade.createProduct(request)
            .let { ProductV1Dto.ViewResponse.from(it) }
            .let { ApiResponse.success(it) }
    }

    @GetMapping("/{productId}")
    fun getProduct(
        @PathVariable productId: Long
    ): ApiResponse<ProductV1Dto.ViewResponse> {
        return productFacade.getProduct(productId)
            .let { ProductV1Dto.ViewResponse.from(it) }
            .let { ApiResponse.success(it) }
    }

    @PutMapping("/{productId}")
    fun updateProduct(
        @PathVariable productId: Long,
        @RequestBody request: ProductV1Dto.UpdateProductRequest
    ): ApiResponse<ProductV1Dto.ViewResponse> {
        return productFacade.updateProduct(productId, request)
            .let { ProductV1Dto.ViewResponse.from(it) }
            .let { ApiResponse.success(it) }
    }

    @DeleteMapping("/{productId}")
    fun deleteProduct(
        @PathVariable productId: Long
    ): ApiResponse<Any> {
        productFacade.deleteProduct(productId)
        return ApiResponse.success()
    }
}
