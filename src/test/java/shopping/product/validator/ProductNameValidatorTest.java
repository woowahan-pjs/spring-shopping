package shopping.product.validator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shopping.common.exception.InvalidProductNameException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ProductNameValidatorTest {

    @Mock
    ProductNameProfanityValidator profanityValidator;

    @InjectMocks
    ProductNameValidator productNameValidator;

    @Test
    void 정상적인_상품_이름이면_검증을_통과한다() {

        //given
        String name = "아이폰 16";

        //when
        productNameValidator.validate(name);

        //then
        verify(profanityValidator).validate(name);
    }

    @Test
    void 허용되지_않은_특수문자가_있으면_예외가_발생한다() {

        //given
        String name = "아이폰@@";

        //when & then
        assertThrows(InvalidProductNameException.class, () -> productNameValidator.validate(name));
    }

    @Test
    void 비속어가_포함되면_예외가_발생한다() {

        //given
        String name = "ass";

        doThrow(new InvalidProductNameException("상품 이름에 비속어가 포함될 수 없습니다."))
                .when(profanityValidator)
                .validate(name);

        //when & then
        assertThrows(InvalidProductNameException.class, () -> productNameValidator.validate(name));
    }
}
