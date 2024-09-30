package shopping.member.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import shopping.global.common.BaseEntity

@Entity
@Table(
    name = "member",
    uniqueConstraints = [UniqueConstraint(name = "uk_member_email", columnNames = ["email"])]
)
class Member(
    email: String,
    loginPassword: String,
    memberType: MemberType
): BaseEntity(), UserDetails {
    @field:Column(name = "email", nullable = false, unique = true)
    var email: String = email
        protected set
    @field:Column(name = "login_password", nullable = false)
    var loginPassword: String = loginPassword
        protected set
    @field:Column(name = "account_type", nullable = false)
    @field:Enumerated(EnumType.STRING)
    var memberType: MemberType = memberType
        protected set

    override fun getAuthorities(): Collection<GrantedAuthority> = listOf(SimpleGrantedAuthority(this.memberType.name))

    override fun getPassword(): String = this.loginPassword

    override fun getUsername(): String? = this.email

    override fun isAccountNonExpired(): Boolean = this.deletedAt == null

    override fun isAccountNonLocked(): Boolean = this.deletedAt == null

    override fun isCredentialsNonExpired(): Boolean = this.deletedAt == null

    override fun isEnabled(): Boolean = this.deletedAt == null
}
