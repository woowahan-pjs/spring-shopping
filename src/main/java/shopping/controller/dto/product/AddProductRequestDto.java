package shopping.controller.dto.product;

public class AddProductRequestDto {
	private String name;
	private Integer price;
	private String imageUrl;

	public AddProductRequestDto(String name, Integer price, String imageUrl) {
		this.name = name;
		this.price = price;
		this.imageUrl = imageUrl;
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
}
