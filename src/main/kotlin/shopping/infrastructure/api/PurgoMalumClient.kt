package shopping.infrastructure.api

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.client.body
import shopping.domain.ProfanityFilter
import shopping.support.circuitbreaker.CircuitBreaker
import shopping.support.circuitbreaker.fallbackIfOpen
import shopping.support.error.CoreException
import shopping.support.error.ErrorType

@Component
class PurgoMalumClient(
    @Qualifier("purgoMalumRestClient") private val restClient: RestClient,
    private val circuitBreaker: CircuitBreaker,
) : ProfanityFilter {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun containsProfanity(text: String): Boolean =
        circuitBreaker
            .run("purgomalum") { executeCheck(text) }
            .fallbackIfOpen { handleCircuitOpen() }
            .getOrElse { handleException(it) }

    private fun executeCheck(text: String): Boolean {
        val response =
            restClient
                .get()
                .uri("/service/containsprofanity?text={text}", text)
                .retrieve()
                .body<String>()

        return response?.toBoolean() ?: false
    }

    private fun handleCircuitOpen(): Boolean {
        log.warn("[Circuit Open] PurgoMalum 비속어 필터링 API 차단됨.")
        throw CoreException(ErrorType.PURGOMALUM_API_ERROR)
    }

    private fun handleException(e: Throwable): Boolean {
        if (e is CoreException) throw e

        log.error("[PURGOMALUM_FAILED] 외부 API 호출 실패", e)
        throw CoreException(ErrorType.PURGOMALUM_API_ERROR)
    }
}
