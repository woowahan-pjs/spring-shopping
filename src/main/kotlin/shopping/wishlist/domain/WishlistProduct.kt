package shopping.wishlist.domain

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.Index
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.io.Serializable
import java.time.LocalDateTime

@Embeddable
data class WishlistProductId(
    @Column
    val memberId: Long,
    @Column
    val productId: Long,
) : Serializable

@Entity
@Table(
    indexes = [
        Index(name = "ix_user_id_product_id", columnList = "memberId, productId"),
    ],
)
class WishlistProduct(
    productId: Long,
    memberId: Long,
) {
    @EmbeddedId
    val id: WishlistProductId = WishlistProductId(memberId, productId)

    @CreationTimestamp
    @Column
    val createdAt: LocalDateTime? = null

    @UpdateTimestamp
    @Column
    var updatedAt: LocalDateTime? = null
        protected set
}
