package shopping.wish.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shopping.common.entity.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "wishes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Wish extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(nullable = false)
    private boolean deleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Builder
    public Wish(Long id, Long memberId, Long productId) {
        this.id = id;
        this.memberId = memberId;
        this.productId = productId;
        this.createdBy = String.valueOf(memberId);
    }

    public static Wish create(Long memberId, Long productId) {
        return Wish.builder()
                   .memberId(memberId)
                   .productId(productId)
                   .build();
    }

    public void delete() {
        this.deleted = true;
        this.deletedAt = LocalDateTime.now();
    }
}
