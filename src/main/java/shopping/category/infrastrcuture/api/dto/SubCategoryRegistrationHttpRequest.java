package shopping.category.infrastrcuture.api.dto;

import shopping.category.application.command.SubCategoryRegistrationCommand;

public class SubCategoryRegistrationHttpRequest {
    private String name;
    private int order;

    private SubCategoryRegistrationHttpRequest() {
    }

    public SubCategoryRegistrationHttpRequest(final String name, final int order) {
        this.name = name;
        this.order = order;
    }

    public SubCategoryRegistrationCommand toCommand(final long mainCategoryId, final long adminId) {
        return new SubCategoryRegistrationCommand(name, order, mainCategoryId, adminId);
    }

    public String getName() {
        return name;
    }

    public int getOrder() {
        return order;
    }

}
