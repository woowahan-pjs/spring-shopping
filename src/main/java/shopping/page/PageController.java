package shopping.page;

import shopping.product.domain.FindProduct;

import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PageController {

    private final FindProduct findProduct;

    public PageController(FindProduct findProduct) {
        this.findProduct = findProduct;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("products", findProduct.execute());
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/products/{id}")
    public String productDetail(@PathVariable UUID id, Model model) {
        model.addAttribute("product", findProduct.execute(id));
        return "product-detail";
    }

    @GetMapping("/wishes")
    public String wishes() {
        return "wishes";
    }
}
