package shopping.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shopping.controller.dto.product.AddProductRequestDto;
import shopping.controller.dto.product.GetProductResponseDto;
import shopping.controller.dto.product.UpdateProductRequestDto;
import shopping.service.ProductService;

@RestController
@RequestMapping("/api/product")
public class ProductController {

	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@PostMapping("/add")
	public ResponseEntity<Object> add(@RequestBody AddProductRequestDto requestDto) {
		productService.addProduct();
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{id}")
	public ResponseEntity<GetProductResponseDto> get(@PathVariable Long id) {
		GetProductResponseDto responseDto = productService.findProduct(id);
		return ResponseEntity.ok(responseDto);
	}

	@GetMapping("/list")
	public ResponseEntity<List<GetProductResponseDto>> list() {
		List<GetProductResponseDto> responseDto = productService.findAllProducts();
		return ResponseEntity.ok(responseDto);
	}

	@PutMapping("/update")
	public ResponseEntity<Object> update(UpdateProductRequestDto requestDto) {
		productService.updateProduct(requestDto);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/delete")
	public ResponseEntity<Object> delete(@PathVariable Long id) {
		productService.deleteProduct(id);
		return ResponseEntity.ok().build();
	}
}
