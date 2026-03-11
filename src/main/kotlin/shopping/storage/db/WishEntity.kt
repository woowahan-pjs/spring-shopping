package shopping.storage.db

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import shopping.domain.Wish

@Entity
@Table(name = "wishes")
class WishEntity(
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
