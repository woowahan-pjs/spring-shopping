package shopping.global.infra

import org.springframework.transaction.TransactionStatus

interface TransactionTemplate {
    fun <T> execute(action: (TransactionStatus) -> T?): T?
}
