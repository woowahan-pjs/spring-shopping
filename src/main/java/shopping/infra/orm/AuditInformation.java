package shopping.infra.orm;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;

import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

// TODO: 생성자, 수정자 Security 설정시 추가 필요
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AuditInformation {

    @CreatedDate
    @Comment("생성 일자")
    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Comment("수정 일자")
    @Column(name = "update_date")
    private LocalDateTime updateDate;
}
