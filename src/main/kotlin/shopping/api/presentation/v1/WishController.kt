package shopping.api.presentation.v1

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import shopping.api.presentation.v1.dto.CreateWishRequest
import shopping.api.presentation.v1.dto.WishResponse
import shopping.api.presentation.v1.validator.WishValidator
import shopping.application.WishService
import shopping.domain.TokenProvider
import shopping.support.response.ApiResponse

@RestController
@RequestMapping("/api/v1/wishes")
class WishController(
    private val wishService: WishService,
    private val wishValidator: WishValidator,
    private val tokenProvider: TokenProvider,
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @RequestHeader("Authorization") authorization: String,
        @RequestBody request: CreateWishRequest,
    ): ApiResponse<WishResponse> {
        wishValidator.validateCreate(request)
        val memberId = extractMemberId(authorization)
        val wishId = wishService.create(request.toCommand(memberId))
        val result = wishService.getWishes(memberId).first { it.id == wishId }
        return ApiResponse.success(WishResponse.from(result))
    }

    @GetMapping
    fun getWishes(
        @RequestHeader("Authorization") authorization: String,
    ): ApiResponse<List<WishResponse>> {
        val memberId = extractMemberId(authorization)
        val results = wishService.getWishes(memberId)
        return ApiResponse.success(results.map { WishResponse.from(it) })
    }

    @DeleteMapping("/{id}")
    fun delete(
        @RequestHeader("Authorization") authorization: String,
        @PathVariable id: Long,
    ): ApiResponse<Any> {
        val memberId = extractMemberId(authorization)
        wishService.delete(id, memberId)
        return ApiResponse.success()
    }

    private fun extractMemberId(authorization: String): Long {
        val token = authorization.removePrefix("Bearer ")
        return tokenProvider.extractMemberId(token)
    }
}
