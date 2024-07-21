package shopping.category.domain;

public record CategoryRegistrationRequest(
        String name,
        int order,
        long adminId
) {
}
