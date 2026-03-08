package shopping.interfaces.api.wish

import com.failsafe.interfaces.api.ApiResponse
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import shopping.application.member.TokenService
import shopping.application.wish.WishFacade

@RestController
@RequestMapping("/api/wishes")
class WishController(
    private val wishFacade: WishFacade,
    private val tokenService: TokenService
) {
    @PostMapping
    fun addWish(
        @RequestHeader("Authorization") token: String,
        @RequestBody request: WishV1Dto.AddWishRequest
    ): ApiResponse<WishV1Dto.WishResponse> {
        val memberId = tokenService.getMemberId(extractToken(token))
        return wishFacade.addWish(memberId, request)
            .let { WishV1Dto.WishResponse.from(it) }
            .let { ApiResponse.success(it) }
    }

    @DeleteMapping("/{wishId}")
    fun removeWish(
        @RequestHeader("Authorization") token: String,
        @PathVariable wishId: Long
    ): ApiResponse<Any> {
        val memberId = tokenService.getMemberId(extractToken(token))
        wishFacade.removeWish(memberId, wishId)
        return ApiResponse.success()
    }

    @GetMapping
    fun getWishes(
        @RequestHeader("Authorization") token: String
    ): ApiResponse<List<WishV1Dto.WishResponse>> {
        val memberId = tokenService.getMemberId(extractToken(token))
        return wishFacade.getWishes(memberId)
            .map { WishV1Dto.WishResponse.from(it) }
            .let { ApiResponse.success(it) }
    }

    private fun extractToken(header: String): String {
        return header.removePrefix("Bearer ")
    }
}
