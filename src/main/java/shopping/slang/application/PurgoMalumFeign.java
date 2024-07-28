package shopping.slang.application;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "PuurgoMalum", url = "${purgo.malum.url}")
public interface PurgoMalumFeign {

    @GetMapping("/containsprofanity")
    String containsSlang(@RequestParam("text") String text);
}
