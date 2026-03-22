package shopping.member.service;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class RegexEmailFormatValidator implements EmailFormatValidator {
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final Pattern PATTERN = Pattern.compile(EMAIL_PATTERN);

    @Override
    public boolean isValid(String email) {
        return PATTERN.matcher(email)
                .matches();
    }
}
