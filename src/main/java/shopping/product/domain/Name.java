package shopping.product.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import shopping.exception.BadRequestException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Embeddable
public class Name {
    private static final String ALLOWED_SPECIAL_CHARS_REGEX = "^[a-zA-Z0-9가-힣 ()\\[\\]+\\-&/_]*$";

    @Column
    private String prdctNm;

    public Name() {
    }

    public Name(String name) {
        validateName(name);
        this.prdctNm = name;
    }

    private void validateName(String name) {
        validateLength(name);
        validateSpecialCharsRegex(name);
    }

    private void validateSpecialCharsRegex(String name) {

        if (!name.matches(ALLOWED_SPECIAL_CHARS_REGEX)) {
            throw new BadRequestException("Value contains invalid characters. Allowed characters are: (),[],+,-,&,/,_");
        }
    }

    private void validateLength(String name) {
        if (name.isBlank() || name.length() > 15) {
            throw new BadRequestException("Name length exceeds 15 characters");
        }
    }


    public String getName() {
        return prdctNm;
    }
}
