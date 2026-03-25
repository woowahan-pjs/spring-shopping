package shopping.member.adapter.in.api;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("[회원] 회원 가입 요청 검증 단위 테스트")
class RegisterRequestTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    @DisplayName("이메일은 254자를 넘길 수 없다")
    void validateEmailLength() {
        String tooLongEmail = "a".repeat(243) + "@example.com";
        RegisterRequest request = new RegisterRequest(tooLongEmail, "password123");

        assertThat(validator.validate(request))
                .extracting(violation -> violation.getPropertyPath().toString())
                .contains("email");
    }
}
