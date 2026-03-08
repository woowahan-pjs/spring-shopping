package shopping.domain.product;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.apache.logging.log4j.util.Strings;
import shopping.domain.product.exception.ProductNameBlankException;
import shopping.domain.product.exception.ProductNameInvalidCharacterException;
import shopping.domain.product.exception.ProductNameLengthExceededException;

import java.util.regex.Pattern;

@Embeddable
public class ProductName {
    private static final int MAX_LENGTH = 15;
    private static final Pattern NAME_REGX = Pattern.compile("^[a-zA-Z0-9가-힣\\s()\\[\\]+\\-&/_]+$");

    @Column(name = "name", nullable = false, length = 15)
    private String name;

    protected ProductName() {
    }

    public ProductName(String name) {
        if(Strings.isEmpty(name)) {
            throw new ProductNameBlankException();
        }

        if(name.length() > MAX_LENGTH) {
            throw new ProductNameLengthExceededException(MAX_LENGTH);
        }

        if(!NAME_REGX.matcher(name).matches()) {
            throw new ProductNameInvalidCharacterException();
        }

        this.name = name;
    }

    public String getValue() {
        return this.name;
    }
}
