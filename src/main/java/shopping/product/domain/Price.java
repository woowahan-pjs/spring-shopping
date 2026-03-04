package shopping.product.domain;

import java.math.BigDecimal;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import org.springframework.util.ObjectUtils;

import com.fasterxml.jackson.annotation.JsonCreator;

@Embeddable
public record Price(
        @NotNull(message = "가격은 필수 입력 값입니다.")
                @DecimalMin(value = "1", message = "가격은 {value}원 이상만 가능합니다.")
                @DecimalMax(value = "99999999999", message = "가격은 {value}원 이하만 가능합니다.")
                BigDecimal value) {

    @JsonCreator
    public static Price create(final Long price) {
        if (ObjectUtils.isEmpty(price)) {
            return null;
        }

        return new Price(BigDecimal.valueOf(price));
    }
}
