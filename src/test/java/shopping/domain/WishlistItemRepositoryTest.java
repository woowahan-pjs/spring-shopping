package shopping.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class WishlistItemRepositoryTest {
    WishlistItemRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryWishlistItemRepository();
    }

    @Test
    @DisplayName("위시리스트를 저장한다")
    void save() {
        WishlistItem wishlistItem = new WishlistItem(1L, 1L);

        WishlistItem saved = repository.save(wishlistItem);

        assertThat(saved.getId()).isNotNull();
    }

    @Test
    @DisplayName("위시리스트를 조회한다")
    void findAllByMemberId() {
        Long memberId = 1L;
        repository.save(new WishlistItem(memberId, 1L));
        repository.save(new WishlistItem(memberId, 2L));
        repository.save(new WishlistItem(memberId, 3L));

        assertThat(repository.findAllByMemberId(memberId).size()).isEqualTo(3);
    }

    @Test
    @DisplayName("위시리스트를 삭제한다")
    void deleteById() {
        Long memberId = 1L;
        WishlistItem wishlistItem = new WishlistItem(memberId, 1L);
        repository.save(wishlistItem);

        repository.deleteById(wishlistItem.getId());

        assertThat(repository.findAllByMemberId(memberId).size()).isEqualTo(0);
    }

    @Test
    @DisplayName("위시리스트에 존재하면 true를 반환한다.")
    void existsByMemberIdAndProductId() {
        WishlistItem wishlistItem = new WishlistItem(1L, 1L);
        repository.save(wishlistItem);

        assertThat(repository.existsByMemberIdAndProductId(1L, 1L)).isTrue();
    }

    @Test
    @DisplayName("위시리스트에 없으면 false를 반환한다.")
    void existsByMemberIdAndProductId2() {
        WishlistItem wishlistItem = new WishlistItem(1L, 1L);
        repository.save(wishlistItem);

        assertThat(repository.existsByMemberIdAndProductId(1L, 2L)).isFalse();
    }
}