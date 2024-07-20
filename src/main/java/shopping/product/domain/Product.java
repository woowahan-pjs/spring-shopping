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
        validateImagePath(imagePath);
        validateAmount(amount);
        validatePrice(price);
    }

    private void validatePrice(final long price) {
        if (price < 0) {
            throw new ProductCreateException("상품 가격은 0보다 커야합니다.");
        }
    }

    private void validateAmount(final int amount) {
        if (amount < 0) {
            throw new ProductCreateException("상품 수량은 0보다 커야합니다.");
        }
    }

    private void validateImagePath(final String imagePath) {
        if (!StringUtils.hasText(imagePath)) {
            throw new ProductCreateException("상품 이미지는 필수값 입니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public String getImagePath() {
        return imagePath;
    }

    public int getAmount() {
        return amount;
    }

    public long getPrice() {
        return price;
    }

    public void modifyName(final String name) {
        this.name = new ProductName(name);
    }

    public void modifyImagePath(final String imagePath) {
        validateImagePath(imagePath);
        this.imagePath = imagePath;
    }

    public void modifyAmount(final int amount) {
        validateAmount(amount);
        this.amount = amount;
    }

    public void modifyPrice(final long price) {
        validatePrice(price);
        this.price = price;
    }

}
