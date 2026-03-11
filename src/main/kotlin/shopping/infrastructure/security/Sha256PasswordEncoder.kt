package shopping.infrastructure.security

import org.springframework.stereotype.Component
import shopping.domain.PasswordEncoder
import java.security.MessageDigest
import java.util.Base64

@Component
class Sha256PasswordEncoder : PasswordEncoder {
    // NOTE: 실무에서는 레인보우 테이블 공격 등을 방지하기 위해 단방향 해시 알고리즘에 솔트(Salt)와 키 스트레칭을 적용하는 BCrypt(또는 Argon2)를 주로 사용합니다.
    override fun encode(rawPassword: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(rawPassword.toByteArray())
        return Base64.getEncoder().encodeToString(hash)
    }

    override fun matches(
        rawPassword: String,
        encodedPassword: String,
    ): Boolean {
        val hashedRawPassword = encode(rawPassword)
        return hashedRawPassword == encodedPassword
    }
}
