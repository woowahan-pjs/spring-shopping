package shopping.global.infra

import org.springframework.stereotype.Component
import org.springframework.transaction.TransactionStatus

@Component
class DefaultTransactionTemplate(
    private val transactionTemplate: org.springframework.transaction.support.TransactionTemplate
) : TransactionTemplate {
    override fun <T> execute(action: (TransactionStatus) -> T?): T? = transactionTemplate.execute(action)
}
