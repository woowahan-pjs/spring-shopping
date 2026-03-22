package shopping.service;

import java.util.List;
import shopping.controller.dto.product.AddProductRequestDto;
import shopping.controller.dto.product.GetProductResponseDto;
import shopping.controller.dto.product.UpdateProductRequestDto;
import shopping.domain.Product;

public interface ProductService {

	void addProduct(AddProductRequestDto requestDto);

	GetProductResponseDto findProduct(Long id);

	List<GetProductResponseDto> findAllProducts();

	List<Product> findProductsByIds(List<Long> ids);

	void updateProduct(UpdateProductRequestDto requestDto);

	void deleteProduct(Long id);
}