package shopping.product.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.util.StringUtils;
import shopping.product.exception.ProductCreateException;

import java.util.regex.Pattern;

@Entity
public class Product {
    private static final Pattern ALLOWED_SPECIAL_CHARACTERS_PATTERN = Pattern.compile("^[a-zA-Z0-9 ()\\[\\]+\\-&/_]*$");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String imagePath;

    private int amount;

    private long price;

    protected Product() {
    }

    public Product(final String name, final String imagePath, final int amount, final long price) {
        validate(name, imagePath, amount, price);
        this.name = name;
        this.imagePath = imagePath;
        this.amount = amount;
        this.price = price;
    }

    private void validate(final String name, final String imagePath, final int amount, final long price) {
        if (!StringUtils.hasText(name)) {
            throw new ProductCreateException("상품 이름은 필수값 입니다.");
        }

        // 이름 길이 검증
        if (name.length() > 15) {
            throw new ProductCreateException("상품 이름은 15자 이하여야 합니다.");
        }

        // 특수 문자 검증
        if (!ALLOWED_SPECIAL_CHARACTERS_PATTERN.matcher(name).matches()) {
            throw new ProductCreateException("상품 이름에 허용되지 않는 특수 문자가 포함되어 있습니다.");
        }

//        // 비속어 필터링 (PurgoMalum API 사용)
//        if (containsProfanity(name)) {
//            throw new ProductCreateException("상품 이름에 비속어가 포함될 수 없습니다.");
//        }

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
