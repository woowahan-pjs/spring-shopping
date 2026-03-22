package shopping.member.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public class Password {

    private String value;

    protected Password() {}

    Password(String encodedValue) {
        this.value = encodedValue;
    }

    public String getValue() {
        return value;
    }
}
