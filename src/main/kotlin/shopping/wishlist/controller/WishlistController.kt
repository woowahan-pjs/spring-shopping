package shopping.wishlist.controller

import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import shopping.common.api.ApiResponse
import shopping.common.domain.CurrentMember
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
        currentMember: CurrentMember,
    ): ApiResponse<AddWishlistResponse> {
        val response = wishlistService.addWishlist(request = request, currentMember = currentMember)

        return ApiResponse.success(response)
    }

    @GetMapping
    fun getWishlists(
        currentMember: CurrentMember,
        @PageableDefault pageable: Pageable,
    ): ApiResponse<Page<WishlistProductDto>> {
        val response = wishlistQueryService.findAllByUserId(currentMember = currentMember, pageable = pageable)

        return ApiResponse.success(response)
    }

    @DeleteMapping("/{productId}")
    fun deleteWishlist(
        currentMember: CurrentMember,
        @PathVariable("productId") productId: Long,
    ): ResponseEntity<ApiResponse<String>> {
        wishlistService.deleteWishlist(productId = productId, currentMember = currentMember)

        return ResponseEntity.noContent().build()
    }
}
