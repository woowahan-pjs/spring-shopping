package shopping.product.presentation

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import shopping.global.common.SuccessResponse
import shopping.product.application.ProductCommandService
import shopping.product.application.ProductQueryService
import shopping.product.presentation.dto.request.ProductRegisterRequest
import shopping.product.presentation.dto.request.ProductModifyRequest
import shopping.product.presentation.dto.response.ProductRegisterResponse
import shopping.product.presentation.dto.response.ProductResponse
import shopping.product.presentation.dto.response.ProductsResponse

@RestController
class ProductApi(
    private val productCommandService: ProductCommandService,
    private val productQueryService: ProductQueryService,
) {
    @PostMapping("/api/products")
    @ResponseStatus(HttpStatus.CREATED)
    fun registerProduct(
        @RequestBody @Valid request: ProductRegisterRequest
    ): SuccessResponse<ProductRegisterResponse> =
        SuccessResponse(ProductRegisterResponse(productCommandService.createProduct(request.toCommand())), HttpStatus.CREATED)

    @PutMapping("/api/products/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun modifyProduct(
        @PathVariable("productId") productId: Long,
        @RequestBody @Valid request: ProductModifyRequest,
    ) = productCommandService.modifyProduct(productId, request.toCommand())

    @DeleteMapping("/api/products/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteProduct(
        @PathVariable("productId") productId: Long
    ) = productCommandService.deletedProduct(productId)

    @GetMapping("/api/products/{productId}")
    fun findProduct(
        @PathVariable("productId") productId: Long
    ): SuccessResponse<ProductResponse> = SuccessResponse(ProductResponse(productQueryService.findProductNotDeleted(productId)))

    @GetMapping("/api/products")
    fun findProducts(): SuccessResponse<ProductsResponse> = SuccessResponse(ProductsResponse(productQueryService.findAllNotDeleted()))
}
