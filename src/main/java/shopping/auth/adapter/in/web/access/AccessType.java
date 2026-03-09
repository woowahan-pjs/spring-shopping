package shopping.auth.adapter.in.web.access;

public enum AccessType {
    PUBLIC,
    MEMBER,
    SELLER;

    public boolean isPublic() {
        return this == PUBLIC;
    }

    public boolean requiresSellerRole() {
        return this == SELLER;
    }
}
