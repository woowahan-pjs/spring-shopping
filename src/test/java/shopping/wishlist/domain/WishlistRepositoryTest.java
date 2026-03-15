package shopping.wishlist.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import shopping.wishlist.infrastructure.WishlistRepositoryImpl;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Import(WishlistRepositoryImpl.class)
class WishlistRepositoryTest {
    @Autowired
    WishlistRepository repository;

    @Test
    @DisplayName("위시리스트를 저장한다")
    void save() {
        Wishlist wishlist = new Wishlist(1L, 1L);

        Wishlist saved = repository.save(wishlist);

        assertThat(saved.getId()).isNotNull();
    }

    @Test
    @DisplayName("위시리스트를 조회한다")
    void findAllByMemberId() {
        Long memberId = 1L;
        repository.save(new Wishlist(memberId, 1L));
        repository.save(new Wishlist(memberId, 2L));
        repository.save(new Wishlist(memberId, 3L));

        assertThat(repository.findAllByMemberId(memberId).size()).isEqualTo(3);
    }

    @Test
    @DisplayName("위시리스트를 삭제한다")
    void deleteById() {
        Long memberId = 1L;
        Wishlist save = repository.save(new Wishlist(memberId, 1L));

        repository.deleteById(save.getId());

        assertThat(repository.findAllByMemberId(memberId)).isEmpty();
    }

    @Test
    @DisplayName("위시리스트에 존재하면 true를 반환한다.")
    void existsByMemberIdAndProductId() {
        Wishlist wishlist = new Wishlist(1L, 1L);
        repository.save(wishlist);

        assertThat(repository.existsByMemberIdAndProductId(1L, 1L)).isTrue();
    }

    @Test
    @DisplayName("위시리스트에 없으면 false를 반환한다.")
    void existsByMemberIdAndProductId2() {
        Wishlist wishlist = new Wishlist(1L, 1L);
        repository.save(wishlist);

        assertThat(repository.existsByMemberIdAndProductId(1L, 2L)).isFalse();
    }
}