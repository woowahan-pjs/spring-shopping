package shopping.product.validator;

import org.springframework.web.client.RestTemplate;
import shopping.common.exception.InvalidProductNameException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class ProductNameProfanityValidator {

    private final RestTemplate restTemplate = new RestTemplate();

    public void validate(String name) {

        String encoded = URLEncoder.encode(name, StandardCharsets.UTF_8);

        String url = "https://www.purgomalum.com/service/containsprofanity?text=" + encoded;
        String containsProfanity = restTemplate.getForObject(url, String.class);

        if ("true".equalsIgnoreCase(containsProfanity)) {
            throw new InvalidProductNameException("상품 이름에 비속어가 포함될 수 없습니다.");
        }
    }
}
