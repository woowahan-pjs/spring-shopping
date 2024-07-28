package shopping.member.client.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import shopping.member.client.domain.Client;
import shopping.member.client.domain.ClientRepository;

public interface JPAClientRepository extends ClientRepository, JpaRepository<Client, String> {


}
