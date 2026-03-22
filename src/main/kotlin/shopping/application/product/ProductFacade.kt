package shopping.application.product

import org.springframework.data.domain.Page
import shopping.domain.product.Product
import shopping.interfaces.api.Paging
import org.springframework.stereotype.Service
import shopping.interfaces.api.product.ProductV1Dto

@Service
class ProductFacade(
    private val productService: ProductService
) {
    fun getProducts(paging: Paging.PagingRequest): Page<Product> {
        return productService.findByPaging(paging.page, paging.size)
    }

    fun getProduct(id: Long): Product {
        return productService.findById(id)
    }

    fun createProduct(request: ProductV1Dto.CreateProductRequest): Product {
        return productService.create(request.name, request.price, request.imageUrl)
    }

    fun updateProduct(id: Long, request: ProductV1Dto.UpdateProductRequest): Product {
        return productService.update(id, request.name, request.price, request.imageUrl)
    }

    fun deleteProduct(id: Long) {
        productService.delete(id)
    }
}
