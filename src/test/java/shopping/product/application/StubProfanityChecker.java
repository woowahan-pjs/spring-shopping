package shopping.product.application;

public class StubProfanityChecker implements ProfanityChecker {
    private final boolean returnValue;

    public StubProfanityChecker(boolean returnValue) {
        this.returnValue = returnValue;
    }

    @Override
    public boolean check(String value) {
        return returnValue;
    }
}
