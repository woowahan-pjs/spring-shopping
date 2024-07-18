package shopping.wishlist.controller

import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import shopping.common.api.ApiResponse
import shopping.common.domain.CurrentUser
import shopping.wishlist.application.AddWishlistRequest
import shopping.wishlist.application.AddWishlistResponse
import shopping.wishlist.application.WishlistService

@RestController
@RequestMapping("/wishlists")
class WishlistController(
    private val wishlistService: WishlistService,
) {
    @PostMapping
    fun addWishlist(
        @Valid @RequestBody request: AddWishlistRequest,
        currentUser: CurrentUser,
    ): ApiResponse<AddWishlistResponse> {
        val response = wishlistService.addWishlist(request = request, currentUser = currentUser)

        return ApiResponse.success(response)
    }
}
