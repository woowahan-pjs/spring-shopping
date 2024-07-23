package shopping.category.infrastrcuture.api.dto;

public class SubCategoryRegistrationHttpResponse {
    private long id;

    public SubCategoryRegistrationHttpResponse() {
    }

    public SubCategoryRegistrationHttpResponse(final long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
