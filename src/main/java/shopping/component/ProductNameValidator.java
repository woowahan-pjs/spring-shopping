package shopping.component;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import shopping.exception.CustomExceptionEnum;
import shopping.exception.NotValidException;

@Component
public class ProductNameValidator {

	private final RestClient restClient = RestClient.create("https://www.purgomalum.com");

	public void validate(String name) {
		hasSlang(name);
		validLength(name);
		validCharacters(name);
	}

	public void hasSlang(String name) {
		String result = restClient.get()
			.uri("/service/plain?text={name}", name)
			.retrieve()
			.body(String.class);
		if(name.equals(result)) {
			return;
		}
		throw new NotValidException(CustomExceptionEnum.NOT_VALID_SPECIAL_CHARACTERS);
	}

	public void validLength(String name) {
		if (name.length() < 16) {
			return;
		}
		throw new NotValidException(CustomExceptionEnum.NOT_VALID_NAME_LENGTH);
	}

	public void validCharacters(String name) {
		if (name.matches("[a-zA-Z0-9가-힣()\\[\\]+=*/_]*")) {
			return;
		}
		throw new NotValidException(CustomExceptionEnum.NOT_VALID_SPECIAL_CHARACTERS);
	}

}
