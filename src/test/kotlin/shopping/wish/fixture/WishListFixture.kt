package shopping.wish.fixture

import shopping.member.domain.Member
import shopping.member.fixture.MemberFixture
import shopping.wish.domain.WishList

enum class WishListFixture(
    val member: MemberFixture,
) {
    `고객 1의 위시 리스트`(MemberFixture.`고객 1`)
    ;

    fun `엔티티 생성`(): WishList = WishList(member.`회원 엔티티 생성`().id)
    fun `엔티티 생성`(member: Member): WishList = WishList(member.id)
}
