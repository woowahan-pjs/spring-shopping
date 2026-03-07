package shopping.service;

import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;
import shopping.component.MemberContext;
import shopping.component.ProductNameValidator;
import shopping.controller.dto.product.AddProductRequestDto;
import shopping.controller.dto.product.GetProductResponseDto;
import shopping.controller.dto.product.UpdateProductRequestDto;
import shopping.domain.Product;
import shopping.exception.CustomExceptionEnum;
import shopping.exception.NotFoundException;
import shopping.repository.ProductRepository;

@Service
public class ProductService {

	private final ProductRepository productRepository;
	private final ProductNameValidator productNameValidator;

	public ProductService(ProductRepository productRepository,
		ProductNameValidator productNameValidator) {
		this.productRepository = productRepository;
		this.productNameValidator = productNameValidator;
	}

	public void addProduct(AddProductRequestDto requestDto) {
		productNameValidator.validate(requestDto.getName());
		Long memberId = MemberContext.getMemberId();
		Product product = Product.create(requestDto.getName(),
										 requestDto.getPrice(),
										 requestDto.getImageUrl(),
										 memberId);
		productRepository.save(product);
	}

	public GetProductResponseDto findProduct(Long id) {
		Product product = productRepository.findById(id)
			.orElseThrow(() -> new NotFoundException(CustomExceptionEnum.NOT_EXIST_PRODUCT));
		return GetProductResponseDto.of(product);
	}

	public List<GetProductResponseDto> findAllProducts() {
		List<Product> products = productRepository.findAll();
		return products.stream()
			.map(GetProductResponseDto::of)
			.toList();
	}

	public List<Product> findProductsByIds(List<Long> ids) {
		return productRepository.findByIdIn(ids);
	}

	@Transactional
	public void updateProduct(UpdateProductRequestDto requestDto) {
		Product product = productRepository.findById(requestDto.getId())
			.orElseThrow(() -> new NotFoundException(CustomExceptionEnum.NOT_EXIST_PRODUCT));
		Long memberId = MemberContext.getMemberId();
		product.update(requestDto.getName(), requestDto.getPrice(), requestDto.getImageUrl(), memberId);
	}

	@Transactional
	public void deleteProduct(Long id) {
		productRepository.deleteById(id);
	}
}
