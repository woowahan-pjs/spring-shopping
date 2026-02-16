package shopping.option;

public record OptionResponse(
    Long id,
    String name,
    int quantity
) {
    public static OptionResponse from(Option option) {
        return new OptionResponse(
            option.getId(),
            option.getName(),
            option.getQuantity()
        );
    }
}
