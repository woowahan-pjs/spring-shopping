package shopping.product.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shopping.common.domain.BaseTimeEntity;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Entity
@Table(name = "products")
public class ProductEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(name = "image_url", nullable = false, columnDefinition = "TEXT")
    private String imageUrl;

    @Column(nullable = false)
    private boolean deleted = false;

    public ProductEntity(String name, int price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public void update(String name, Integer price, String imageUrl) {
        if (name != null) this.name = name;
        if (price != null) this.price = price;
        if (imageUrl != null) this.imageUrl = imageUrl;
    }

    public void delete() {
        this.deleted = true;
    }
}
