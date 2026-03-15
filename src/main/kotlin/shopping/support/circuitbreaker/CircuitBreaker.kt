package shopping.support.circuitbreaker

import io.github.resilience4j.circuitbreaker.CallNotPermittedException
import org.slf4j.LoggerFactory
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory
import org.springframework.stereotype.Component

class CircuitOpenException(
    message: String = "Circuit breaker is open",
) : RuntimeException(message)

interface CircuitBreaker {
    fun <T> run(
        name: String = "default",
        block: () -> T,
    ): Result<T>
}

@Component
class DefaultCircuitBreaker(
    private val factory: CircuitBreakerFactory<*, *>,
) : CircuitBreaker {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun <T> run(
        name: String,
        block: () -> T,
    ): Result<T> {
        val circuitBreaker = factory.create(name)

        return kotlin.runCatching {
            circuitBreaker.run(block) { e ->
                log.warn("[CircuitBreaker: {}] 실행 중 예외 발생: {}", name, e.message)
                throw e.convertToCustomException()
            }
        }
    }
}

// NOTE: Result<T> 확장 함수 - 서킷이 열린 경우(CircuitOpenException)에만 Fallback 실행
fun <T> Result<T>.fallbackIfOpen(function: (e: Throwable?) -> T): Result<T> {
    val exception = this.exceptionOrNull()
    if (exception is CircuitOpenException) {
        return runCatching { function(exception) }
    }
    return this
}

// NOTE: Resilience4j 예외를 커스텀 예외로 변환
fun Throwable.convertToCustomException(): Throwable {
    if (this is CallNotPermittedException) {
        return CircuitOpenException()
    }
    return this
}
