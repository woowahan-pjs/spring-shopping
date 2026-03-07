package shopping.controller.dto.product;

import shopping.domain.Product;

public class GetProductResponseDto {
	private Long id;
	private String name;
	private Integer price;
	private String imageUrl;

	public GetProductResponseDto(Long id, String name, Integer price, String imageUrl) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.imageUrl = imageUrl;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Integer getPrice() {
		return price;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public static GetProductResponseDto of(Product product) {
		return new GetProductResponseDto(product.getId(), product.getName(), product.getPrice(), product.getImageUrl());
	}
}
