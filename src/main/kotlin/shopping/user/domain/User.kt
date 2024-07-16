package shopping.user.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import shopping.common.domain.BaseEntity

@Entity
@Table(name = "users")
class User(
    email: String,
    password: EncryptedPassword,
) : BaseEntity() {
    @Column(unique = true)
    val email: String = email

    @Column
    var password: EncryptedPassword = password
}
