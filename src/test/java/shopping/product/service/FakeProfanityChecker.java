package shopping.product.service;

import shopping.common.client.ProfanityChecker;
import shopping.common.exception.ProfanityServiceUnavailableException;

public class FakeProfanityChecker implements ProfanityChecker {
    private boolean profane = false;
    private boolean unavailable = false;

    public void setProfane(boolean profane) {
        this.profane = profane;
    }

    public void setUnavailable(boolean unavailable) {
        this.unavailable = unavailable;
    }

    @Override
    public boolean containsProfanity(String text) {
        if (unavailable) {
            throw new ProfanityServiceUnavailableException("비속어 검증 서비스를 사용할 수 없습니다.", null);
        }
        return profane;
    }
}
