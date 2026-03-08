package shopping.domain.product

interface ProductBadWordClient {
    fun containsProfanity(text: String): Boolean
}
