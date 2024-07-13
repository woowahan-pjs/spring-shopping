package shopping.product.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String imageUrl;

    protected Product() {
    }

    Product(final String name, final String imageUrl) {
        validateLength(name);
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public static Product from(ProductCreate productCreate) {
        return new Product(productCreate.name(), productCreate.imageUrl());
    }

    private void validateLength(final String name) {
        if (name.length() > 15) {
            throw new InvalidProductNameLengthException();
        }
    }
}
