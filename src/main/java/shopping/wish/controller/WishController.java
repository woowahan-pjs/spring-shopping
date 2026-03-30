package shopping.wish.controller;

import shopping.wish.dto.WishResponse;
import shopping.wish.service.WishApplicationService;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishes")
public class WishController {

    private final WishApplicationService wishApplicationService;

    public WishController(WishApplicationService wishApplicationService) {
        this.wishApplicationService = wishApplicationService;
    }

    @PostMapping("/{productId}")
    public ResponseEntity<WishResponse> add(@RequestHeader("Authorization") String authorization,
            @PathVariable UUID productId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(wishApplicationService.add(authorization, productId));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> remove(@RequestHeader("Authorization") String authorization,
            @PathVariable UUID productId) {
        wishApplicationService.remove(authorization, productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<WishResponse>> findAll(
            @RequestHeader("Authorization") String authorization) {
        return ResponseEntity.ok(wishApplicationService.findAll(authorization));
    }

}
