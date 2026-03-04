package shopping.product.domain;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.hibernate.validator.constraints.URL;

@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.RECORD_COMPONENT})
@Retention(RetentionPolicy.RUNTIME)
@URL(message = "http:// 또는 https:// 형식만 입력 가능합니다.")
@Size(min = 9, max = 255, message = "이미지 URL은 최소 {min}자에서 최대 {max}자 까지 입력 가능합니다.")
@NotBlank(message = "이미지 URL은 필수 입력 값입니다.")
public @interface ValidProductImageUrl {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
