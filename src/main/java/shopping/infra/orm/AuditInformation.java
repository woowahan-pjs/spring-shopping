package shopping.infra.orm;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;

import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AuditInformation {

    @CreatedBy
    @Comment("생성자")
    @Column(name = "create_id", columnDefinition = "bigint", nullable = false, updatable = false)
    private Long createId;

    @CreatedDate
    @Comment("생성 일자")
    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @LastModifiedBy
    @Comment("수정자")
    @Column(name = "update_id", columnDefinition = "bigint")
    private Long updateId;

    @LastModifiedDate
    @Comment("수정 일자")
    @Column(name = "update_date")
    private LocalDateTime updateDate;
}
