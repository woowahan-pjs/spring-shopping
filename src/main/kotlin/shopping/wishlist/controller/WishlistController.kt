package shopping.wishlist.controller

import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import shopping.common.api.ApiResponse
import shopping.common.domain.CurrentUser
import shopping.wishlist.application.AddWishlistRequest
import shopping.wishlist.application.AddWishlistResponse
import shopping.wishlist.application.WishlistProductDto
import shopping.wishlist.application.WishlistQueryService
import shopping.wishlist.application.WishlistService

@RestController
@RequestMapping("/wishlists")
class WishlistController(
    private val wishlistService: WishlistService,
    private val wishlistQueryService: WishlistQueryService,
) {
    @PostMapping
    fun addWishlist(
        @Valid @RequestBody request: AddWishlistRequest,
        currentUser: CurrentUser,
    ): ApiResponse<AddWishlistResponse> {
        val response = wishlistService.addWishlist(request = request, currentUser = currentUser)

        return ApiResponse.success(response)
    }

    @GetMapping
    fun getWishlists(
        currentUser: CurrentUser,
        @PageableDefault pageable: Pageable,
    ): ApiResponse<Page<WishlistProductDto>> {
        val response = wishlistQueryService.findAllByUserId(currentUser = currentUser, pageable = pageable)

        return ApiResponse.success(response)
    }
}
