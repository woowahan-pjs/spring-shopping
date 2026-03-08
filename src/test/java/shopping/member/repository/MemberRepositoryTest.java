package shopping.member.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import shopping.member.domain.Member;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 회원을_저장한다() {
        //given
        Member member = Member.create("test@test.com", "123456");

        //when
        Member saved = memberRepository.save(member);

        //then
        assertThat(saved.getEmail()).isEqualTo("test@test.com");
    }

    @Test
    void 이메일로_회원을_조회한다() {
        //given
        Member member = Member.create("find@test.com", "123456");
        memberRepository.save(member);

        //when
        Member found = memberRepository.findByEmail("find@test.com").orElseThrow();

        //then
        assertThat(found.getEmail()).isEqualTo("find@test.com");
    }

    @Test
    void 이메일_존재여부를_확인한다() {
        //given
        Member member = Member.create("exists@test.com", "123456");
        memberRepository.save(member);

        //when
        boolean exists = memberRepository.existsByEmail("exists@test.com");

        //then
        assertThat(exists).isTrue();
    }
}
