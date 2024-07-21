package shopping.token.domain;

public class JwtToken implements Token {
    private final String value;

    private JwtToken(String value) {
        this.value = value;
    }

    public static JwtToken from(String value) {
        return new JwtToken(value);
    }

    @Override
    public String getValue() {
        return value;
    }
}
