package shopping.product.infra

import org.slf4j.LoggerFactory
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.RequestEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClientException
import org.springframework.web.client.exchange
import shopping.product.domain.BadWordValidator
import java.lang.RuntimeException

@Component
class PurgomalumClient(
    private val restTemplateBuilder: RestTemplateBuilder,
) : BadWordValidator {
    private val logger = LoggerFactory.getLogger(PurgomalumClient::class.java)
    private val restTemplate = restTemplateBuilder.build()
    private val badWordMarker = '*'

    override fun isBadWord(text: String): Boolean {
        val requestEntity = createRequestEntity(text)

        try {
            logger.info("{}", requestEntity)
            val response = restTemplate.exchange<String>(requestEntity)
            logger.info("{}", response)
            val body = response.body ?: throw RuntimeException("Purgomalum API의 응답 body가 없음")
            return body.contains(badWordMarker)
        } catch (e: Exception) {
            when (e) {
                is RestClientException -> throw RuntimeException("Purgomalum API network error", e)
                else -> throw RuntimeException("Purgomalum API 호출중 알수없는 예외 발생", e)
            }
        }
    }

    private fun createRequestEntity(text: String) =
        RequestEntity
            .get("https://www.purgomalum.com/service/plain?text=$text")
            .build()
}
