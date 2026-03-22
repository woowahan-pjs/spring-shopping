package shopping.domain.wish

import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.hibernate.annotations.SQLDelete
import shopping.support.config.jpa.BaseEntity
import java.math.BigDecimal

@Table(name = "wishes")
@Entity
@SQLDelete(sql = "UPDATE wishes SET deleted_at = NOW() WHERE id = ?")
class Wish(
    memberId: Long,
    productId: Long
) : BaseEntity() {
    var memberId: Long = memberId
        protected set
    var productId: Long = productId
        protected set
}