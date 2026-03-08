package shopping.wish.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shopping.common.auth.AuthRole;
import shopping.common.auth.LoginMember;
import shopping.common.auth.RoleRequired;
import shopping.common.dto.ApiResponse;
import shopping.wish.application.command.WishAddUseCase;
import shopping.wish.application.command.WishDeleteUseCase;
import shopping.wish.application.dto.WishAddRequest;
import shopping.wish.application.dto.WishAddResponse;
import shopping.wish.application.dto.WishGetResponse;
import shopping.wish.application.query.WishQueryService;

import java.util.List;

@RestController
@RequestMapping("/api/wishes")
@RequiredArgsConstructor
public class WishController {

    private final WishAddUseCase wishAddUseCase;
    private final WishDeleteUseCase wishDeleteUseCase;
    private final WishQueryService wishQueryService;

    @RoleRequired(AuthRole.USER)
    @PostMapping
    public ResponseEntity<ApiResponse<WishAddResponse>> add(
        @LoginMember Long memberId,
        @Valid @RequestBody WishAddRequest request) {
        WishAddResponse response = wishAddUseCase.execute(memberId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.of(HttpStatus.CREATED, response));
    }

    @RoleRequired(AuthRole.USER)
    @DeleteMapping("/{wishId}")
    public ResponseEntity<ApiResponse<Long>> delete(
        @LoginMember Long memberId,
        @PathVariable Long wishId) {
        Long response = wishDeleteUseCase.execute(memberId, wishId);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @RoleRequired(AuthRole.USER)
    @GetMapping
    public ResponseEntity<ApiResponse<List<WishGetResponse>>> getAll(
        @LoginMember Long memberId) {
        List<WishGetResponse> response = wishQueryService.getAll(memberId);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }
}
