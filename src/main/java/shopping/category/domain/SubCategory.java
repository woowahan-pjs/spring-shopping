package shopping.category.domain;

public class SubCategory {
    private final Long id;
    private final String name;
    private final long order;
    private final long createdBy;
    private final long modifiedBy;

    public SubCategory(final Long id, final String name, final long order, final long createdBy, final long modifiedBy) {
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
}
