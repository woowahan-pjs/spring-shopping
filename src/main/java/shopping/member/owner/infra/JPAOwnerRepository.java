package shopping.member.owner.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import shopping.member.owner.domain.Owner;
import shopping.member.owner.domain.OwnerRepository;

public interface JPAOwnerRepository extends OwnerRepository, JpaRepository<Owner, String> {

}
