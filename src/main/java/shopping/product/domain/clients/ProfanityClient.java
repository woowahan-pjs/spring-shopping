package shopping.product.domain.clients;

@FunctionalInterface
public interface ProfanityClient {
    boolean containProfanity(final String word);
}
