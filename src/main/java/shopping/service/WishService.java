package shopping.service;

import java.util.List;
import org.springframework.stereotype.Service;
import shopping.component.MemberContext;
import shopping.controller.dto.Wish.GetWishResponseDto;
import shopping.domain.Product;
import shopping.domain.Wish;
import shopping.repository.WishRepository;

@Service
public class WishService {
	private final ProductService productService;
	private final WishRepository wishRepository;

	public WishService(ProductService productService, WishRepository wishRepository) {
		this.productService = productService;
		this.wishRepository = wishRepository;
	}

	public List<GetWishResponseDto> findAll() {
		Long memberId = MemberContext.getMemberId();
		List<Wish> wishes = wishRepository.findAllByMemberId(memberId);
		List<Long> productIds = wishes.stream()
			.map(Wish::getProductId)
			.toList();
		List<Product> products = productService.findProductsByIds(productIds);
		return products.stream()
			.map(GetWishResponseDto::of)
			.toList();
	}

	public void delete(Long id) {
		wishRepository.deleteById(id);
	}

	public void addWish(Long productId) {
		productService.findProduct(productId);
		Wish wish = Wish.of(MemberContext.getMemberId(), productId);
		wishRepository.save(wish);
	}
}
