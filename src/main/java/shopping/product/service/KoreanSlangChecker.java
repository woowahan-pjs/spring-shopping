package shopping.product.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class KoreanSlangChecker {
    private final List<String> safePhrases;
    private final List<String> blockedHangulTerms;
    private final List<String> blockedAsciiTerms;

    public KoreanSlangChecker(
            @Value("${app.slang.korean.rules-location}") Resource rulesResource
    ) {
        SlangRules rules = loadRules(rulesResource);
        this.safePhrases = rules.safePhrases();
        this.blockedHangulTerms = rules.blockedHangulTerms();
        this.blockedAsciiTerms = rules.blockedAsciiTerms();
    }

    public boolean containsSlang(String text) {
        String normalizedHangulText = SlangTextNormalizer.normalizeHangul(text);
        String hangulTextWithoutSafePhrases = removeSafePhrases(normalizedHangulText);
        if (containsBlockedHangulSlang(hangulTextWithoutSafePhrases)) {
            return true;
        }

        String normalizedAsciiText = SlangTextNormalizer.normalizeAscii(text);
        return containsBlockedAsciiSlang(normalizedAsciiText);
    }

    private String removeSafePhrases(String text) {
        String sanitized = text;
        for (String safePhrase : safePhrases) {
            sanitized = sanitized.replace(safePhrase, "");
        }
        return sanitized;
    }

    private boolean containsBlockedHangulSlang(String text) {
        return containsBlockedTerm(text, blockedHangulTerms);
    }

    private boolean containsBlockedAsciiSlang(String text) {
        return containsBlockedTerm(text, blockedAsciiTerms);
    }

    private boolean containsBlockedTerm(String text, List<String> blockedTerms) {
        return blockedTerms.stream().anyMatch(text::contains);
    }

    private SlangRules loadRules(Resource resource) {
        EnumMap<RuleType, List<String>> rulesByType = initializeRulesByType();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)
        )) {
            int lineNumber = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                String trimmedLine = line.trim();
                if (trimmedLine.isBlank() || isHeader(trimmedLine)) {
                    continue;
                }
                addRule(trimmedLine, lineNumber, rulesByType);
            }
            return new SlangRules(
                    List.copyOf(rulesByType.get(RuleType.SAFE_PHRASE)),
                    List.copyOf(rulesByType.get(RuleType.BLOCKED_HANGUL)),
                    List.copyOf(rulesByType.get(RuleType.BLOCKED_ASCII))
            );
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to load slang rules from " + resource.getDescription(), exception);
        }
    }

    private EnumMap<RuleType, List<String>> initializeRulesByType() {
        EnumMap<RuleType, List<String>> rulesByType = new EnumMap<>(RuleType.class);
        for (RuleType ruleType : RuleType.values()) {
            rulesByType.put(ruleType, new ArrayList<>());
        }
        return rulesByType;
    }

    private boolean isHeader(String line) {
        return "type,term".equalsIgnoreCase(line);
    }

    private void addRule(String line, int lineNumber, EnumMap<RuleType, List<String>> rulesByType) {
        String[] columns = line.split(",", 2);
        if (columns.length != 2) {
            throw invalidRule(lineNumber, "Each line must have the format 'type,term'.");
        }

        RuleType ruleType = RuleType.from(columns[0], lineNumber);
        String term = columns[1].trim();
        if (term.isBlank()) {
            throw invalidRule(lineNumber, "Rule term must not be blank.");
        }
        rulesByType.get(ruleType).add(term);
    }

    private IllegalStateException invalidRule(int lineNumber, String message) {
        return new IllegalStateException("Invalid slang rule at line " + lineNumber + ". " + message);
    }

    private enum RuleType {
        SAFE_PHRASE,
        BLOCKED_HANGUL,
        BLOCKED_ASCII;

        private static RuleType from(String rawType, int lineNumber) {
            try {
                return RuleType.valueOf(rawType.trim().toUpperCase());
            } catch (IllegalArgumentException exception) {
                throw new IllegalStateException(
                        "Invalid slang rule at line " + lineNumber + ". Unknown rule type: " + rawType
                );
            }
        }
    }

    private record SlangRules(
            List<String> safePhrases,
            List<String> blockedHangulTerms,
            List<String> blockedAsciiTerms
    ) {
    }
}
