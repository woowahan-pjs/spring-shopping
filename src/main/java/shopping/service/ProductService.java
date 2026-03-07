package shopping.service;

import java.util.List;
import org.springframework.stereotype.Service;
import shopping.controller.dto.product.AddProductRequestDto;
import shopping.controller.dto.product.GetProductResponseDto;
import shopping.controller.dto.product.UpdateProductRequestDto;
import shopping.repository.ProductRepository;

@Service
public class ProductService {

	private final ProductRepository productRepository;

	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public void addProduct(AddProductRequestDto requestDto) {

	}

	public GetProductResponseDto findProduct(Long id) {
		return null;
	}

	public List<GetProductResponseDto> findAllProducts() {
		return null;
	}

	public void updateProduct(UpdateProductRequestDto requestDto) {

	}

	public void deleteProduct(Long id) {

	}
}
