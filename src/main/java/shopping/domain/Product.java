package shopping.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private Integer price;
	private String imageUrl;
	private Long createdBy;
	private Long updatedBy;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	protected Product() {
	}

	public Product(Long id, String name, Integer price, String imageUrl, Long createdBy,
		Long updatedBy,
		LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.imageUrl = imageUrl;
		this.createdBy = createdBy;
		this.updatedBy = updatedBy;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public static Product create(String name, Integer price, String imageUrl, Long memberId) {
		return new Product(null, name, price, imageUrl, memberId, null, LocalDateTime.now(), null);
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

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void update(String name, Integer price, String imageUrl, Long memberId) {
		this.name = name;
		this.price = price;
		this.imageUrl = imageUrl;
		this.updatedBy = memberId;
		this.updatedAt = LocalDateTime.now();
	}
}
