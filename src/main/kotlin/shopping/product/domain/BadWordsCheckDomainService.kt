package shopping.product.domain

import org.springframework.stereotype.Service

@Service
class BadWordsCheckDomainService {
    fun isBadWords(text: String): Boolean = false
}
