package shopping.product.application

import org.springframework.stereotype.Service
import shopping.product.infra.ProfanityRepository

@Service
class ProfanityValidator(private val profanityRepository: ProfanityRepository) {
    fun containsProfanity(productName: String): Boolean =
        profanityRepository.postContainsProfanity(productName).toBoolean()
}
