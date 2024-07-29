package shopping;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Entity
@Getter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 15)
    @Pattern(regexp = "^[\\w\\s\\-\\&\\(\\)\\[\\]/+가-힣]+$", message = "Invalid product name")
    private String name;

    private String imageUrl;

    public void update(Product productDetails) {
        this.name = productDetails.getName();
        this.imageUrl = productDetails.getImageUrl();
    }
}
