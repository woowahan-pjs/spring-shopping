package shopping.product.adapter.out;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "purgomalum", url = "${app.slang.purgomalum.base-url}")
public interface PurgomalumFeignClient {
    @GetMapping("/service/containsprofanity")
    String containsSlang(@RequestParam("text") String text);
}
