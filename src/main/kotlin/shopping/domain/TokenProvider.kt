package shopping.domain

interface TokenProvider {
    fun generate(memberId: Long): String
}
