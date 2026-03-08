package shopping.domain.product;

import org.apache.logging.log4j.util.Strings;
import shopping.domain.product.exception.ProductNameBlankException;
import shopping.domain.product.exception.ProductNameInvalidCharacterException;
import shopping.domain.product.exception.ProductNameLengthExceededException;

import java.util.regex.Pattern;

public class ProductName {
    private static final int MAX_LENGTH = 15;
    private static final Pattern NAME_REGX = Pattern.compile("^[a-zA-Z0-9가-힣\\s()\\[\\]+\\-&/_]+$");

    private final String name;

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
}
