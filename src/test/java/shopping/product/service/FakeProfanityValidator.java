package shopping.product.service;

public class FakeProfanityValidator implements ProfanityValidator {

    @Override
    public boolean containsProfanity(String text) {
        return "dickhead".equals(text);
    }
}
