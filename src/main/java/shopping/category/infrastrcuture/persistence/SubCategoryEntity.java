package shopping.category.infrastrcuture.persistence;

import jakarta.persistence.*;

import java.util.Objects;

@Table(name = "sub_categories")
@Entity
public class SubCategoryEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @Column(name = "orders")
    private long order;

    @Column(name = "created_by")
    private long createdBy;

    @Column(name = "modified_by")
    private long modifiedBy;

    @JoinColumn(name = "main_category_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private MainCategoryEntity mainCategory;

    public SubCategoryEntity() {
    }

    public SubCategoryEntity(final Long id, final String name, final long order, final long createdBy, final long modifiedBy) {
        this.id = id;
        this.name = name;
        this.order = order;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getOrder() {
        return order;
    }

    public long getCreatedBy() {
        return createdBy;
    }

    public long getModifiedBy() {
        return modifiedBy;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final SubCategoryEntity that = (SubCategoryEntity) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
