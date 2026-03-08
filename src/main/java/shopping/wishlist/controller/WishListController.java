package shopping.wishlist.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import shopping.infra.security.UserPrincipal;
import shopping.wishlist.dto.WishListResponse;
import shopping.wishlist.service.WishListService;

@SecurityRequirement(name = "JWT")
@Validated
@Tag(name = "[위시 리스트] 위시 리스트 관리 API", description = "위시 리스트에 대한 관리를 담당합니다.")
@RestController
@RequestMapping("/api/wishes")
@RequiredArgsConstructor
class WishListController {

    private final WishListService wishListService;

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "상품 조회 성공"),
                @ApiResponse(responseCode = "401", description = "인증 정보가 없을 때"),
                @ApiResponse(responseCode = "403", description = "유효하지 않은 권한의 회원이 요청했을 때"),
                @ApiResponse(responseCode = "500", description = "서버 오류")
            })
    @Operation(summary = "위시 리스트 조회", description = "등록한 위시 리스트 정보를 조회 합니다.")
    public WishListResponse getWishList(
            @AuthenticationPrincipal final UserPrincipal userPrincipal) {
        return wishListService.getWishList(userPrincipal.getId());
    }
}
