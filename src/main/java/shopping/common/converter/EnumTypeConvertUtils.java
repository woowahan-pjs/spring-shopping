package shopping.common.converter;

import java.util.EnumSet;
import org.apache.commons.lang3.StringUtils;

public class EnumTypeConvertUtils {
    public static <E extends Enum<E> & EnumType> E ofCode(Class<E> enumClass, String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }

        return EnumSet.allOf(enumClass).stream()
                      .filter(it -> StringUtils.equals(it.getCode(), code.trim()))
                      .findAny()
                      .orElseThrow(() -> new IllegalArgumentException("Enum not found. Enum: " + enumClass.getName() + ", code: " + code));
    }

    public static <E extends Enum<E> & EnumType> String toCode(E enumValue) {
        if (enumValue == null) {
            return null;
        }

        return enumValue.getCode();
    }
}
