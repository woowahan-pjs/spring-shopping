package shopping.category.infrastrcuture.api.dto;

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
