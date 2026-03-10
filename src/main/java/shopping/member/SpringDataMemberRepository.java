package shopping.member;

import shopping.member.domain.Member;
import shopping.member.domain.MemberRepository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpringDataMemberRepository
        extends MongoRepository<Member, UUID>, MemberRepository {

    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);
}
