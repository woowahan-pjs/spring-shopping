package shopping.category.api.dto;

import shopping.category.application.command.CategoryRegistrationCommand;

public class CategoryRegistrationHttpRequest {
    private String name;
    private int order;

    public CategoryRegistrationHttpRequest() {
    }

    public CategoryRegistrationHttpRequest(String name, int order) {
        this.name = name;
        this.order = order;
    }

    public CategoryRegistrationCommand toCommand(final long adminId) {
        return new CategoryRegistrationCommand(name, order, adminId);
    }

    public String getName() {
        return name;
    }

    public int getOrder() {
        return order;
    }
}
