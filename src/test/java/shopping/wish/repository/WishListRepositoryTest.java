package shopping.wish.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.util.ReflectionTestUtils;
import shopping.member.domain.Member;
import shopping.product.domain.Product;
import shopping.product.repository.ProductRepository;
import shopping.wish.domain.WishList;
import shopping.wish.domain.WishListItem;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class WishListRepositoryTest {

    @Autowired
    private WishListRepository wishListRepository;

    @Autowired
    private WishListItemRepository wishListItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestEntityManager em;

    @Test
    void memberId로_위시리스트를_조회한다() {

        Member member = Member.create("test@test.com", "12345678");
        em.persist(member);

        WishList wishList = WishList.create(member);
        ReflectionTestUtils.setField(wishList, "member", member);

        em.persist(wishList);

        Optional<WishList> result = wishListRepository.findByMemberId(member.getId());

        assertThat(result).isPresent();
    }

    @Test
    void 위시리스트_아이템을_삭제한다() {

        Member member = Member.create("test@test.com", "12345678");

        WishList wishList = WishList.create(member);
        em.persist(wishList);

        Product product = Product.create("아이폰", 1_000_000);
        em.persist(product);

        WishListItem item = WishListItem.create(wishList, product);
        em.persist(item);

        Long id = item.getId();

        wishListItemRepository.deleteById(id);

        em.flush();
        em.clear();

        WishListItem result = em.find(WishListItem.class, id);

        assertThat(result).isNull();
    }

    @Test
    void 위시리스트_아이템을_조회한다() {

        Member member = Member.create("test@test.com", "12345678");

        WishList wishList = wishListRepository.save(WishList.create(member));
        Product product = productRepository.save(Product.create("아이폰", 1_000_000));

        WishListItem item = WishListItem.create(wishList, product);
        wishListItemRepository.save(item);

        List<WishListItem> result = wishListItemRepository.findByWishListId(wishList.getId());

        assertThat(result).hasSize(1);
    }
}
