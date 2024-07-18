package shopping.wishlist.domain

import org.springframework.data.jpa.repository.JpaRepository

interface WishlistProductRepository : JpaRepository<WishlistProduct, WishlistProductId>
