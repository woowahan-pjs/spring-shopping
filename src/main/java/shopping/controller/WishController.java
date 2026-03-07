package shopping.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shopping.controller.dto.Wish.GetWishResponseDto;
import shopping.service.WishService;

@RestController
@RequestMapping("/api/wish")
public class WishController {

	private final WishService wishService;

	public WishController(WishService wishService) {
		this.wishService = wishService;
	}

	@GetMapping("/list")
	public ResponseEntity<List<GetWishResponseDto>> list() {
		List<GetWishResponseDto> responseDto = wishService.findAll();
		return ResponseEntity.ok(responseDto);
	}

	@PostMapping("/add")
	public ResponseEntity<Object> add(@RequestBody Long productId) {
		wishService.addWish(productId);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/delete")
	public ResponseEntity<Object> delete(@PathVariable Long id) {
		wishService.delete(id);
		return ResponseEntity.ok().build();
	}
}
