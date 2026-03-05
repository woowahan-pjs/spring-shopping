package shopping.common.converter;

import jakarta.persistence.AttributeConverter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EnumTypeConverter<E extends Enum<E> & EnumType> implements AttributeConverter<E, String> {
    private final Class<E> targetEnumClass;

    @Override
    public String convertToDatabaseColumn(E attribute) {
        return EnumTypeConvertUtils.toCode(attribute);
    }

    @Override
    public E convertToEntityAttribute(String s) {
        return EnumTypeConvertUtils.ofCode(targetEnumClass, s);
    }
}
