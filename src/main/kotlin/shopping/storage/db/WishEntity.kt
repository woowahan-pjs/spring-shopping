package shopping.storage.db

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import shopping.domain.Wish

@Entity
@Table(name = "wishes")
class WishEntity(
    // NOTE: @ManyToOne 연관 매핑 대신 ID만 보관하여 도메인 간 결합도를 낮춥니다.
    //       멀티 모듈 분리 시 모듈 간 Entity 직접 참조를 방지할 수 있습니다.
    @Column(nullable = false)
    val memberId: Long,
    @Column(nullable = false)
    val productId: Long,
) : BaseEntity() {
    fun toDomain(): Wish =
        Wish(
            id = this.id,
            memberId = this.memberId,
            productId = this.productId,
        )

    companion object {
        fun from(wish: Wish): WishEntity =
            WishEntity(
                memberId = wish.memberId,
                productId = wish.productId,
            )
    }
}
