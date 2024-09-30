package shopping.product.infra

import org.springframework.stereotype.Repository
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.HttpExchange

@Repository
@HttpExchange
interface ProfanityRepository {
    @GetExchange("/service/containsprofanity")
    fun postContainsProfanity(@RequestParam("text") productName: String): String
}
