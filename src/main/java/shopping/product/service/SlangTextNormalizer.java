package shopping.product.service;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public final class SlangTextNormalizer {
    private static final Pattern NON_HANGUL_PATTERN = Pattern.compile("[^가-힣ㄱ-ㅎㅏ-ㅣᄀ-ᇿ]");
    private static final Pattern NON_ASCII_ALPHA_NUMERIC_PATTERN = Pattern.compile("[^a-z0-9]");

    private SlangTextNormalizer() {
    }

    public static String normalizeHangul(String text) {
        String lowered = text.toLowerCase(Locale.ROOT);
        String hangulOnly = NON_HANGUL_PATTERN.matcher(lowered).replaceAll("");
        return collapseRepeatedCharacters(hangulOnly);
    }

    public static String normalizeAscii(String text) {
        String normalized = normalizeUnicode(text);
        return NON_ASCII_ALPHA_NUMERIC_PATTERN.matcher(normalized).replaceAll("");
    }

    private static String normalizeUnicode(String text) {
        return Normalizer.normalize(text, Normalizer.Form.NFKC).toLowerCase(Locale.ROOT);
    }

    private static String collapseRepeatedCharacters(String text) {
        StringBuilder builder = new StringBuilder();
        char previous = 0;
        for (int index = 0; index < text.length(); index++) {
            char current = text.charAt(index);
            if (current == previous) {
                continue;
            }
            builder.append(current);
            previous = current;
        }
        return builder.toString();
    }
}
