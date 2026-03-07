package shopping.product;

import java.util.regex.Pattern;

public class CreateProductService implements CreateProduct {

    private static final int MAX_NAME_LENGTH = 15;
    private static final Pattern ALLOWED_NAME_PATTERN =
            Pattern.compile("^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ ()+\\-]+$");

    private final ProductRepository productRepository;
    private final ProfanityChecker profanityChecker;

    public CreateProductService(ProductRepository productRepository,
            ProfanityChecker profanityChecker) {
        this.productRepository = productRepository;
        this.profanityChecker = profanityChecker;
    }

    @Override
    public Product execute(Product product) {
        validateName(product.getName());
        return productRepository.save(product);
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("상품 이름은 필수입니다.");
        }
        if (name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("상품 이름은 공백 포함 15자 이하여야 합니다.");
        }
        if (!ALLOWED_NAME_PATTERN.matcher(name).matches()) {
            throw new IllegalArgumentException("상품 이름에 허용되지 않는 특수문자가 포함되어 있습니다.");
        }
        if (profanityChecker.containsProfanity(name)) {
            throw new IllegalArgumentException("상품 이름에 비속어가 포함되어 있습니다.");
        }
    }
}
