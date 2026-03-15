package shopping.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class ProductTest {

	@Test
	void Product_엔티티필드_정확하게_매핑되어있다() {
		Product product = Product.create("상품명", 10000, "http://image.url", 1L);

		assertThat(product.getId()).isNull();
		assertThat(product.getName()).isEqualTo("상품명");
		assertThat(product.getPrice()).isEqualTo(10000);
		assertThat(product.getImageUrl()).isEqualTo("http://image.url");
		assertThat(product.getCreatedBy()).isEqualTo(1L);
		assertThat(product.getUpdatedAt()).isNull();
	}

}