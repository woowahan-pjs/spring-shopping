package shopping.category.infrastrcuture.persistence;

import jakarta.persistence.*;

@Table(name = "sub_categories")
@Entity
public class SubCategoryEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public SubCategoryEntity() {
    }

    public Long getId() {
        return id;
    }
}
