package shopping.service;

import java.util.regex.Pattern;

public class RegexEmailFormatValidator implements EmailFormatValidator {
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    @Override
    public boolean isValid(String email) {
        return Pattern.compile(EMAIL_PATTERN)
                .matcher(email)
                .matches();
    }
}
