package shopping.wishlist.domain

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.io.Serializable
import java.time.LocalDateTime

@Embeddable
class WishlistProductId(
    @Column
    val productId: Long,
    @Column
    val userId: Long,
) : Serializable

@Entity
class WishlistProduct(
    productId: Long,
    userId: Long,
) {
    @EmbeddedId
    val id = WishlistProductId(productId, userId)

    @CreationTimestamp
    @Column
    val createdAt: LocalDateTime? = null

    @UpdateTimestamp
    @Column
    var updatedAt: LocalDateTime? = null
        protected set
}
