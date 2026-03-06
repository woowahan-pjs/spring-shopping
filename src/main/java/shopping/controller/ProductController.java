package shopping.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product")
public class ProductController {

	@PostMapping("/add")
	public void add() {

	}

	@GetMapping("/list")
	public void list() {

	}

	@PutMapping("/update")
	public void update() {

	}

	@GetMapping("/delete")
	public void delete() {

	}
}
