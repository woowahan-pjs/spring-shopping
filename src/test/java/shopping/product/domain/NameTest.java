package shopping.product.domain;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import shopping.exception.BadRequestException;

import static org.assertj.core.api.Assertions.assertThat;

public class NameTest {

    @DisplayName("이름을 생성한다.")
    @ParameterizedTest(name = "{index} | {displayName} | {argumentsWithNames}")
    @ValueSource(strings = {"맛있는 음식", "delicious food", "대박 1 good", "[대박] 맛집 V1"})
    void createName(String input) {
        Name name = Name.from(input);
        assertThat(name).isNotNull();
    }

    @DisplayName("이름이 같은 객체는 동등하다.")
    @Test
    void equals() {
        Name name1 = Name.from("빠숑");
        Name name2 = Name.from("빠숑");

        assertThat(name1).isEqualTo(name2);
    }



    @DisplayName("이름이 15자 이상일 때 예외를 발생한다.")
    @ParameterizedTest(name = "{index} | {displayName} | {argumentsWithNames}")
    @ValueSource(strings = {"이것은 열다섯자 이상 이름입니다", "this is over 15 length"})
    void over15LengthException(String input) {
        Assertions.assertThatThrownBy(() -> Name.from(input))
                .isInstanceOf(BadRequestException.class)
                .hasMessageStartingWith("Name length exceeds 15 characters");
    }

    @DisplayName("(),[],+,-,&,/,_ 이외의 특수문자를 포함할 경우 예외를 발생한다.")
    @ParameterizedTest(name = "{index} | {displayName} | {argumentsWithNames}")
    @ValueSource(strings = {"@맛있은 음식", "[대박 맛집] $", "#최고의 맛"})
    void containInvalidSpecialCharsException(String input) {
        Assertions.assertThatThrownBy(() -> Name.from(input))
                .isInstanceOf(BadRequestException.class)
                .hasMessageStartingWith("Value contains invalid characters");
    }
}
