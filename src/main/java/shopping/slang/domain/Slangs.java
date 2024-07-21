package shopping.slang.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import shopping.product.domain.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Slangs {
    @Builder.Default
    private List<Slang> slangs = new ArrayList<>();

    public List<String> getSlangsValueList() {
        return slangs.stream()
                .map(Slang::getSlang)
                .collect(Collectors.toList());
    }
}
