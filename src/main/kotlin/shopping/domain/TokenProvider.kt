package shopping.domain

interface TokenProvider {
    fun generate(memberId: Long): String

    fun extractMemberId(token: String): Long
}
