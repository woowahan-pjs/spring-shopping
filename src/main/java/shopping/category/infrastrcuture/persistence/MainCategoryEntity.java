package shopping.category.infrastrcuture.persistence;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Table(name = "main_categories")
@Entity
public class MainCategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @Column(name = "orders")
    private int order;

    @Column(name = "created_by")
    private long createdBy;

    @Column(name = "modified_by")
    private long modifiedBy;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubCategoryEntity> subCategoryEntities;

    public MainCategoryEntity() {
    }

    public MainCategoryEntity(final Long id, final String name, final int order, final long createdBy, final long modifiedBy, final List<SubCategoryEntity> subCategoryEntities) {
        this.id = id;
        this.name = name;
        this.order = order;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
        this.subCategoryEntities = subCategoryEntities;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getOrder() {
        return order;
    }

    public long getCreatedBy() {
        return createdBy;
    }

    public long getModifiedBy() {
        return modifiedBy;
    }

    public List<SubCategoryEntity> getSubCategoryEntities() {
        return subCategoryEntities;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final MainCategoryEntity that = (MainCategoryEntity) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
