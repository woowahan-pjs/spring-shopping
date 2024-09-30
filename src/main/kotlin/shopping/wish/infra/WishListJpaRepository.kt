package shopping.wish.infra

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import shopping.wish.domain.WishList

@Repository
interface WishListJpaRepository : JpaRepository<WishList, Long>, KotlinJdslJpqlExecutor
