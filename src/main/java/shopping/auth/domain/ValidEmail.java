package shopping.auth.domain;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.RECORD_COMPONENT})
@Retention(RetentionPolicy.RUNTIME)
@Email(message = "유효한 이메일 형식이 아닙니다.")
@Size(min = 5, max = 320, message = "이메일은 최소 {min}자에서 최대 {max}자 까지 입력 가능합니다.")
@NotBlank(message = "이메일은 필수 입력 값입니다.")
public @interface ValidEmail {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
