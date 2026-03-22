package shopping.domain.member

import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.hibernate.annotations.SQLDelete
import shopping.support.config.jpa.BaseEntity
import shopping.support.error.CoreException
import shopping.support.error.ErrorType

@Table(name = "members")
@Entity
@SQLDelete(sql = "UPDATE members SET deleted_at = NOW() WHERE id = ?")
class Member(
    email: String,
    password: String
) : BaseEntity() {
    var email: String = email
        protected set

    var password: String = password
        protected set

    init {
        if (email.isBlank()) throw CoreException(ErrorType.BAD_REQUEST, "이메일은 비어있을 수 없습니다.")
        if (password.isBlank()) throw CoreException(ErrorType.BAD_REQUEST, "비밀번호는 비어있을 수 없습니다.")
    }

    fun checkPassword(password: String): Boolean {
        return password.equals(this.password)
    }

}