package shopping.product.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ProductName {

    @Column(name = "product_name", nullable = false)
    private String name;

    protected ProductName(){
    }

    public ProductName(String name, ProfanityChecker profanityChecker) {
        if(profanityChecker.isProfanity(name)){
            throw new InvalidProductNameException("상품 이름에 비속어를 사용할 수 없습니다. " + name);
        }

        this.name = name;
    }
}
