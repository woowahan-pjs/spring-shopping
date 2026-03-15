package shopping.controller.dto.Wish;

import shopping.domain.Product;

public class GetWishResponseDto {
	private Long productId;
	private String productName;
	private String imageUrl;
	private Integer price;

	private GetWishResponseDto(Long productId, String productName, String imageUrl, Integer price) {
		this.productId = productId;
		this.productName = productName;
		this.imageUrl = imageUrl;
		this.price = price;
	}

	public Long getProductId() {
		return productId;
	}

	public String getProductName() {
		return productName;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public Integer getPrice() {
		return price;
	}

	public static GetWishResponseDto of(Product product) {
		return new GetWishResponseDto(product.getId(), product.getName(), product.getImageUrl(), product.getPrice());
	}
}
