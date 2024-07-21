package shopping.category.api.dto;

public class CategoryRegistrationHttpResponse {
    private long id;

    public CategoryRegistrationHttpResponse() {
    }

    public CategoryRegistrationHttpResponse(final long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
