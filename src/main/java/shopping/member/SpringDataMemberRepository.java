package shopping.member;

import shopping.member.domain.Member;
import shopping.member.domain.MemberRepository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SpringDataMemberRepository extends JpaRepository<Member, UUID>, MemberRepository {

    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);

    @Modifying
    @Query("DELETE FROM Wish w WHERE w.productId = :productId")
    @Override
    void removeWishByProductId(UUID productId);
}
