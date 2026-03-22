package shopping.application.product

import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import shopping.domain.product.Product
import shopping.interfaces.api.Paging
import shopping.interfaces.api.product.ProductV1Dto

@Service
class ProductFacade(
    private val productService: ProductService,
) {
    fun getProducts(paging: Paging.PagingRequest): Page<Product> = productService.findByPaging(paging.page, paging.size)

    fun getProduct(id: Long): Product = productService.findById(id)

    fun createProduct(request: ProductV1Dto.CreateProductRequest): Product =
        productService.create(request.name, request.price, request.imageUrl)

    fun updateProduct(
        id: Long,
        request: ProductV1Dto.UpdateProductRequest,
    ): Product = productService.update(id, request.name, request.price, request.imageUrl)

    fun deleteProduct(id: Long) {
        productService.delete(id)
    }
}
