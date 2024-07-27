package shopping.wish.presentation

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import shopping.auth.application.LoginMember
import shopping.global.common.SuccessResponse
import shopping.member.domain.Member
import shopping.wish.application.WishListCommandService
import shopping.wish.application.WishListQueryService
import shopping.wish.presentation.dto.request.WishProductAddRequest
import shopping.wish.presentation.dto.request.WishProductDeleteRequest
import shopping.wish.presentation.dto.response.WishListResponse

@RestController
class WishListApi(
    private val wishListCommandService: WishListCommandService,
    private val wishListQueryService: WishListQueryService,
) {
    @PostMapping("/api/wishes/products")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun addWishListProduct(@LoginMember member: Member, @RequestBody @Valid request: WishProductAddRequest) = wishListCommandService.addWishProduct(member.id, request.toCommand())

    @DeleteMapping("/api/wishes/products")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteWishListProduct(@LoginMember member: Member, @RequestBody @Valid request: WishProductDeleteRequest) = wishListCommandService.deleteWishProduct(member.id, request.toCommand())

    @GetMapping("/api/wishes")
    fun getWishList(@LoginMember member: Member): SuccessResponse<WishListResponse> = SuccessResponse(WishListResponse(wishListQueryService.findWishList(member.id)))
}
