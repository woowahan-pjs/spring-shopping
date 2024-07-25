package shopping.product.infra

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import shopping.product.domain.Product

@Repository
interface ProductJpaRepository : JpaRepository<Product, Long>, KotlinJdslJpqlExecutor
