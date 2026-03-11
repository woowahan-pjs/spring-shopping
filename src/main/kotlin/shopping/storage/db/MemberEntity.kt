package shopping.storage.db

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import shopping.domain.Member

@Entity
@Table(name = "members")
class MemberEntity(
    @Column(nullable = false, unique = true)
    val email: String,
    @Column(nullable = false)
    val password: String,
) : BaseEntity() {
    fun toDomain(): Member =
        Member(
            id = this.id,
            email = this.email,
            password = this.password,
        )

    companion object {
        fun from(member: Member): MemberEntity =
            MemberEntity(
                email = member.email,
                password = member.password,
            )
    }
}
