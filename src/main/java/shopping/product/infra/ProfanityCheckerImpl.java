package shopping.product.infra;

import org.springframework.stereotype.Component;
import shopping.product.application.ProfanityChecker;

@Component
public class ProfanityCheckerImpl implements ProfanityChecker {
    private final ProfanityCheckClient profanityCheckClient;

    public ProfanityCheckerImpl(ProfanityCheckClient profanityCheckClient) {
        this.profanityCheckClient = profanityCheckClient;
    }

    /**
     * @param value 검사 하고자 하는 텍스트
     * @return 텍스트를 보내면 Purgomalum에서 true or false를 내려준다.
     */
    @Override
    public boolean check(String value) {
        return profanityCheckClient.check(value);
    }
}
