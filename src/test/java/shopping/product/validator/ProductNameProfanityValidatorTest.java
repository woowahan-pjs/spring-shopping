package shopping.product.validator;

import org.junit.jupiter.api.Test;
import shopping.common.exception.InvalidProductNameException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProductNameProfanityValidatorTest {

    ProductNameProfanityValidator profanityValidator = new ProductNameProfanityValidator();

    @Test
    void 비속어가_포함된_상품_이름이면_예외가_발생한다() {

        //given
        String name = "ass";

        // when & phone
        assertThrows(InvalidProductNameException.class, () -> profanityValidator.validate(name));
    }

    @Test
    void 정상적인_상품_이릉이면_예외가_발생하지_않는다() {

        //given
        String name = "apple juice";

        //when & then
        assertDoesNotThrow(() -> profanityValidator.validate(name));
    }
}
