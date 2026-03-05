package shopping.wish.api;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shopping.auth.web.annotation.MemberOnly;
import shopping.auth.web.annotation.CurrentMemberId;
import shopping.wish.service.WishService;

@RestController
@RequestMapping("/api/wishes")
@RequiredArgsConstructor
@MemberOnly
public class WishController {
    private final WishService wishService;

    @PostMapping
    public ResponseEntity<WishResponse> add(
            @CurrentMemberId Long memberId,
            @Valid @RequestBody WishCreateRequest request
    ) {
        WishResponse response = wishService.add(memberId, request.productId(), request.quantity());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{wishId}")
    public ResponseEntity<Void> delete(
            @CurrentMemberId Long memberId,
            @PathVariable Long wishId
    ) {
        wishService.delete(memberId, wishId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<WishResponse>> list(@CurrentMemberId Long memberId) {
        List<WishResponse> response = wishService.list(memberId);
        return ResponseEntity.ok(response);
    }
}
