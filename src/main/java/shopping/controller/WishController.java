package shopping.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wish")
public class WishController {
	@GetMapping("/list")
	public void list() {

	}

	@GetMapping("/add")
	public void add() {

	}

	@DeleteMapping("/delete")
	public void delete() {

	}
}
