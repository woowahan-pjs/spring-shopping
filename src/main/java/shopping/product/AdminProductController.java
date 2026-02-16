package shopping.product;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shopping.category.Category;
import shopping.category.CategoryRepository;

import java.util.List;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public AdminProductController(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "product/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        return "product/new";
    }

    @PostMapping
    public String create(
        @RequestParam String name,
        @RequestParam int price,
        @RequestParam(required = false) String imageUrl,
        @RequestParam Long categoryId,
        Model model
    ) {
        List<String> errors = ProductNameValidator.validate(name, true);
        if (!errors.isEmpty()) {
            populateNewForm(model, errors, name, price, imageUrl, categoryId);
            return "product/new";
        }

        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new IllegalArgumentException("카테고리가 존재하지 않습니다. id=" + categoryId));
        productRepository.save(new Product(name, price, imageUrl, category));
        return "redirect:/admin/products";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다. id=" + id));
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryRepository.findAll());
        return "product/edit";
    }

    @PostMapping("/{id}/edit")
    public String update(
        @PathVariable Long id,
        @RequestParam String name,
        @RequestParam int price,
        @RequestParam(required = false) String imageUrl,
        @RequestParam Long categoryId,
        Model model
    ) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다. id=" + id));

        List<String> errors = ProductNameValidator.validate(name, true);
        if (!errors.isEmpty()) {
            populateEditForm(model, product, errors, name, price, imageUrl, categoryId);
            return "product/edit";
        }

        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new IllegalArgumentException("카테고리가 존재하지 않습니다. id=" + categoryId));

        product.update(name, price, imageUrl, category);
        productRepository.save(product);
        return "redirect:/admin/products";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        productRepository.deleteById(id);
        return "redirect:/admin/products";
    }

    private void populateNewForm(
        Model model,
        List<String> errors,
        String name,
        int price,
        String imageUrl,
        Long categoryId
    ) {
        model.addAttribute("errors", errors);
        model.addAttribute("name", name);
        model.addAttribute("price", price);
        model.addAttribute("imageUrl", imageUrl);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("categories", categoryRepository.findAll());
    }

    private void populateEditForm(
        Model model,
        Product product,
        List<String> errors,
        String name,
        int price,
        String imageUrl,
        Long categoryId
    ) {
        model.addAttribute("errors", errors);
        model.addAttribute("product", product);
        model.addAttribute("name", name);
        model.addAttribute("price", price);
        model.addAttribute("imageUrl", imageUrl);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("categories", categoryRepository.findAll());
    }
}
