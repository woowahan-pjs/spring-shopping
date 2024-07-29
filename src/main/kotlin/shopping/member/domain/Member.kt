package shopping.member.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import shopping.common.domain.BaseEntity

@Entity
class Member(
    email: String,
    password: EncryptedPassword,
) : BaseEntity() {
    @Column(unique = true)
    val email: String = email

    @Column
    var password: EncryptedPassword = password
}
