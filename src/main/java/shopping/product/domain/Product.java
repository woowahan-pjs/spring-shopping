package shopping.product.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.util.StringUtils;
import shopping.product.exception.ProductCreateException;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = ProductName.MIN_LENGTH)
    private ProductName name;

    @Column(nullable = false)
    private String imagePath;

    private int amount;

    private long price;

    protected Product() {
    }

    public Product(final String name, final String imagePath, final int amount, final long price) {
        validate(imagePath, amount, price);
        this.name = new ProductName(name);
        this.imagePath = imagePath;
        this.amount = amount;
        this.price = price;
    }

    private void validate(final String imagePath, final int amount, final long price) {
        if (!StringUtils.hasText(imagePath)) {
            throw new ProductCreateException("상품 이미지는 필수값 입니다.");
        }
        if (amount < 0) {
            throw new ProductCreateException("상품 수량은 0보다 커야합니다.");
        }
        if (price < 0) {
            throw new ProductCreateException("상품 가격은 0보다 커야합니다.");
        }
    }
}
