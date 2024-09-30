package shopping.fake

import org.springframework.transaction.TransactionStatus
import shopping.global.infra.TransactionTemplate

class FakeTransactionTemplate : TransactionTemplate {
    override fun <T> execute(action: (TransactionStatus) -> T?): T? = action(FAKE_TRANSACTION_STATUS)

    companion object {
        private val FAKE_TRANSACTION_STATUS = object : TransactionStatus {
            override fun createSavepoint(): Any {
                return Any()
            }

            override fun rollbackToSavepoint(savepoint: Any) {
            }

            override fun releaseSavepoint(savepoint: Any) {
            }

        }
    }
}
