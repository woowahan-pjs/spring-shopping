package shopping.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Wish {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long memberId;
	private Long productId;


	protected Wish() {
	}

	private Wish(Long id, Long memberId, Long productId) {
		this.id = id;
		this.memberId = memberId;
		this.productId = productId;
	}

	public Long getId() {
		return id;
	}

	public Long getMemberId() {
		return memberId;
	}

	public Long getProductId() {
		return productId;
	}

	public static Wish of(Long memberId, Long productId) {
		return new Wish(null, memberId, productId);
	}
}
