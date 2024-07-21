package shopping.slang.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import shopping.slang.domain.Slang;
import shopping.slang.domain.Slangs;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlangRequest {

    @Size(min = 1)
    private List<String> values;

    public Slangs toSlangs(Slangs persistSlangs) {
        validateValues(persistSlangs);
        return new Slangs(addSlangs());
    }

    private void validateValues(Slangs persistSlangs) {
        removeAllValueBlank();
        removePersistSlangs(persistSlangs.getSlangsValueList());
    }

    private void removeAllValueBlank() {
        values = values.stream()
                .map(this::removeBlank)
                .collect(Collectors.toList());
    }

    private void removePersistSlangs(List<String> persistSlangs) {
        List<String> remainValues = new ArrayList<>(values);
        remainValues.removeAll(persistSlangs);
        values = remainValues;
    }

    private String removeBlank(String s) {
        return s.replaceAll(" ", "");
    }

    private List<Slang> addSlangs() {
        return values.stream()
                .map(this::toSlang)
                .collect(Collectors.toList());
    }

    private Slang toSlang(String value) {
        return Slang.builder()
                .slang(value)
                .build();
    }
}
