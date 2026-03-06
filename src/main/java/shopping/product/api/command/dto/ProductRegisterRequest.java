package shopping.product.api.command.dto;

public record ProductRegisterRequest(String name, Long price, String imageUrl) {
}
