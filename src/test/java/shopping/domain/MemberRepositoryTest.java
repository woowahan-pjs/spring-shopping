package shopping.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberRepositoryTest {
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        memberRepository = new InMemoryMemberRepository();
    }

    @Test
    @DisplayName("회원을 저장한다.")
    void save() {
        Member saved = memberRepository.save(createMember());

        assertThat(saved.getId()).isNotNull();
    }

    @Test
    @DisplayName("회원을 이메일로 조회한다.")
    void findByEmail() {
        Member saved = memberRepository.save(createMember());

        Member found = memberRepository.findByEmail(saved.getEmail());

        assertThat(found).isEqualTo(saved);
    }

    private Member createMember() {
        return new Member("test@gmail.com", "password");
    }
}
