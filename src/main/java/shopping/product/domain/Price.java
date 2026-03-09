package shopping.product.domain;

import java.math.BigDecimal;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;

import org.springframework.util.ObjectUtils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import shopping.infra.exception.ShoppingBusinessException;

@Embeddable
public record Price(
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

    public static Price create(final String price) {
        try {
            return create(Long.parseLong(price));
        } catch (NumberFormatException e) {
            throw new ShoppingBusinessException("잘못된 형식의 입력 값입니다.");
        }
    }

    @JsonValue
    @Override
    public BigDecimal value() {
        return value;
    }
}
