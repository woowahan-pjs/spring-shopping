package shopping.product.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.util.StringUtils;
import shopping.product.exception.InvalidProductException;

import java.util.Objects;

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
        this(null, name, imagePath, amount, price);
    }

    public Product(final Long id, final String name, final String imagePath, final int amount, final long price) {
        validate(imagePath, amount, price);
        this.id = id;
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
            throw new InvalidProductException("상품 가격은 0보다 커야합니다.");
        }
    }

    private void validateAmount(final int amount) {
        if (amount < 0) {
            throw new InvalidProductException("상품 수량은 0보다 커야합니다.");
        }
    }

    private void validateImagePath(final String imagePath) {
        if (!StringUtils.hasText(imagePath)) {
            throw new InvalidProductException("상품 이미지는 필수값 입니다.");
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

    public void modify(final Product product) {
        validate(product.imagePath, product.amount, product.price);
        this.name = new ProductName(product.getName());
        this.imagePath = product.getImagePath();
        this.amount = product.getAmount();
        this.price = product.getPrice();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
