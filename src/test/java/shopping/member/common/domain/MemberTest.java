package shopping.member.common.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import shopping.fake.FakePasswordEncoder;
import shopping.member.client.domain.Client;

@DisplayName("Member")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MemberTest {

    private final PasswordEncoder passwordEncoder = new FakePasswordEncoder();

    @Test
    void 올바른_Password인지_확인할_수_있다() {
        final Member member = createMember();

        assertThat(member.isValidPassword("1111", passwordEncoder)).isFalse();
    }

    @Test
    void Member가_어떤_권한을_가졌는지_얻을_수_있다() {
        final Member member = createMember();

        assertThat(member.getRole()).isEqualTo("Client");
    }

    @Test
    void Member가_유효한_권한을_가졌는지_확인할_수_있다() {
        final Member member = createMember();

        assertThat(member.isValidRole("Client")).isTrue();
    }

    private Member createMember() {
        final Password password = new Password("1234", passwordEncoder);
        return new Client("test@test.com", password, "test");
    }
}