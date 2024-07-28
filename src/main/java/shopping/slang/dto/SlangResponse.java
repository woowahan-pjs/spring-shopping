package shopping.slang.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import shopping.slang.domain.Slang;
import shopping.slang.domain.Slangs;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SlangResponse {
    private List<String> values;

    public static SlangResponse toSlangResponse(Slangs slangs) {
        return SlangResponse.builder()
                .values(addValues(slangs.getSlangs()))
                .build();
    }

    private static List<String> addValues(List<Slang> slangs) {
        return slangs.stream()
                .map(Slang::getSlang)
                .collect(Collectors.toList());
    }
}
