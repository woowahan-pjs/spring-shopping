package shopping.product.domain;

import static org.assertj.core.api.Assertions.assertThatNoException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import shopping.fake.FakeProfanityChecker;

@DisplayName("Product")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ProductTest {

    private ProfanityChecker profanityChecker = new FakeProfanityChecker();

    @Test
    void Product를_생성할_수_있다() {
        assertThatNoException()
                .isThrownBy(() -> new Product("맥북", profanityChecker, 1_000L, "image.jpg"));
    }
}