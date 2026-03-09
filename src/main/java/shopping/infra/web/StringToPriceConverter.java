package shopping.infra.web;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import shopping.product.domain.Price;

@Component
class StringToPriceConverter implements Converter<String, Price> {

    @Override
    public Price convert(final String value) {
        return Price.create(value);
    }
}
