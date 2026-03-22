package shopping.page;

import shopping.product.domain.FindProduct;
import shopping.product.domain.Product;
import shopping.product.dto.ProductResponse;

import java.util.List;
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
        List<ProductResponse> products =
                findProduct.execute().stream().map(ProductResponse::from).toList();
        model.addAttribute("products", products);
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/products/{id}")
    public String productDetail(@PathVariable UUID id, Model model) {
        Product product = findProduct.execute(id);
        model.addAttribute("product", ProductResponse.from(product));
        return "product-detail";
    }

    @GetMapping("/wishes")
    public String wishes() {
        return "wishes";
    }
}
