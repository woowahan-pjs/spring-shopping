package shopping.user.domain

import shopping.common.auth.AuthUtil

const val MIN_PASSWORD_LENGTH = 10
const val MAX_PASSWORD_LENGTH = 20

@JvmInline
value class EncryptedPassword private constructor(
    private val value: String,
) {
    companion object {
        fun from(plainPassword: String): EncryptedPassword {
            require(plainPassword.length in MIN_PASSWORD_LENGTH..MAX_PASSWORD_LENGTH) {
                "비밀번호는 $MIN_PASSWORD_LENGTH~$MAX_PASSWORD_LENGTH 길이여야 합니다."
            }

            return EncryptedPassword(AuthUtil.encryptSha256(plainPassword))
        }
    }
}
