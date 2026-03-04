package shopping.product.domain;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.RECORD_COMPONENT})
@Retention(RetentionPolicy.RUNTIME)
@Pattern(
        regexp = "^[\\p{L}\\p{M}\\p{N} ()\\[\\]&\\-+/_,]+$",
        message = "특수문자는 (, ), [, ], &, -, +, /, _`만 가능합니다.")
@Size(min = 5, max = 15, message = "상품명은 최소 {min}자에서 최대 {max}자 까지 입력 가능합니다.")
@NotBlank(message = "상품명은 필수 입력 값입니다.")
public @interface ValidProductName {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
