package shopping.wishlist.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shopping.infra.security.UserPrincipal;
import shopping.wishlist.dto.WishListResponse;
import shopping.wishlist.dto.WishListSaveRequest;
import shopping.wishlist.dto.WishListSaveSummary;
import shopping.wishlist.service.WishListSaveService;
import shopping.wishlist.service.WishListService;

@SecurityRequirement(name = "JWT")
@Validated
@Tag(name = "[위시 리스트] 위시 리스트 관리 API", description = "위시 리스트에 대한 관리를 담당합니다.")
@RestController
@RequestMapping("/api/wishes")
@RequiredArgsConstructor
class WishListController {

    private final WishListService wishListService;

    private final WishListSaveService wishListSaveService;

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "위시 리스트 조회 성공"),
        @ApiResponse(responseCode = "401", description = "인증 정보가 없을 때"),
        @ApiResponse(responseCode = "403", description = "유효하지 않은 권한의 회원이 요청했을 때"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @Operation(summary = "위시 리스트 조회", description = "등록한 위시 리스트 정보를 조회 합니다.")
    public WishListResponse getWishList(@AuthenticationPrincipal final UserPrincipal userPrincipal) {
        return wishListService.getWishList(userPrincipal.getId());
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "위시 리스트 항목 저장 결과"),
        @ApiResponse(responseCode = "401", description = "인증 정보가 없을 때"),
        @ApiResponse(responseCode = "403", description = "유효하지 않은 권한의 회원이 요청했을 때"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @Operation(summary = "위시 리스트 항목 저장", description = "위시 리스트에 원하는 상품을 저장합니다.")
    public WishListSaveSummary registerWishList(
            @AuthenticationPrincipal final UserPrincipal userPrincipal,
            @RequestBody @Valid final WishListSaveRequest request) {
        return wishListSaveService.registerWishList(userPrincipal.getId(), request);
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @DeleteMapping("/{wishId}")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "위시 리스트 항목 삭제 성공"),
        @ApiResponse(responseCode = "400", description = "입력 값이 잘못 되었거나, 항목이 없는 경우"),
        @ApiResponse(responseCode = "401", description = "인증 정보가 없을 때"),
        @ApiResponse(responseCode = "403", description = "유효하지 않은 권한의 회원이 요청했을 때"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @Operation(summary = "위시 리스트 항목 삭제", description = "등록한 위시 리스트 항목을 삭제 합니다.")
    public void removeWishList(
            @AuthenticationPrincipal final UserPrincipal userPrincipal,
            @PathVariable @Min(value = 1, message = "위시 리스트 항목 고유 ID는 0보다 큰 값이여야 합니다.") final Long wishId) {
        wishListService.removeWishList(userPrincipal.getId(), wishId);
    }
}
