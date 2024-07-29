package shopping.product.domain

interface BadWordValidator {
    fun isBadWord(text: String): Boolean
}
