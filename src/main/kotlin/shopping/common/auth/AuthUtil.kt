package shopping.common.auth

import java.security.MessageDigest

object AuthUtil {
    private val sha256Digest = MessageDigest.getInstance("SHA-256")

    fun encryptSha256(text: String): String =
        sha256Digest.digest(text.toByteArray()).joinToString("") {
            "%02x".format(it)
        }
}
