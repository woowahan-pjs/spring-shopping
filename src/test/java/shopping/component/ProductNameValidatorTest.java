package shopping.component;

import org.junit.jupiter.api.Test;
import shopping.exception.NotValidException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThatNoException;

class ProductNameValidatorTest {

    private final ProductNameValidator validator = new ProductNameValidator();

    @Test
    void hasSlang_비속어_포함시_예외발생() {
        assertThatThrownBy(() -> validator.hasSlang("this is fucking test input"))
                .isInstanceOf(NotValidException.class);
    }

    @Test
    void hasSlang_비속어_미포함시_정상() {
        assertThatNoException().isThrownBy(() -> validator.hasSlang("this is clean input"));
    }

	@Test
	void validLength_문자길이_15글자_이상_예외발생() {
		assertThatThrownBy(() -> validator.validLength("this is test input"))
				.isInstanceOf(NotValidException.class);
	}

	@Test
	void validCharacters_허용된_특수문자_정상() {
		assertThatNoException().isThrownBy(() -> validator.validCharacters("()[]+=*/+_"));
	}

	@Test
	void validCharacters_허용되지_않은_특수문자_사용시_예외발생() {
		assertThatThrownBy(() -> validator.validCharacters("-"))
					.isInstanceOf(NotValidException.class);
	}
}