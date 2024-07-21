package shopping.acceptance.utils;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfanityTestController {
    @GetMapping("/containsprofanity")
    public String checkProfanity(@RequestParam String text) {
        if (text != null && text.contains("비속어")) {
            return "true";
        }

        return "false";
    }
}
