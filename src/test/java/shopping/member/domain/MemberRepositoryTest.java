package shopping.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import shopping.member.infrastructure.MemberRepositoryImpl;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(MemberRepositoryImpl.class)
public class MemberRepositoryTest {
    @Autowired
    private MemberRepository repository;

    @Test
    @DisplayName("회원을 저장한다.")
    void save() {
        Member saved = repository.save(createMember());

        assertThat(saved.getId()).isNotNull();
    }

    @Test
    @DisplayName("회원을 이메일로 조회한다.")
    void findByEmail() {
        Member saved = repository.save(createMember());

        Member found = repository.findByEmail(saved.getEmail()).get();

        assertThat(found).isEqualTo(saved);
    }

    private Member createMember() {
        return new Member("test@gmail.com", "password");
    }
}
