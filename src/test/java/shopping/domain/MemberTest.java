package shopping.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import shopping.exception.NotValidException;

class MemberTest {

	@Test
	void create_이메일_비밀번호_이름_필드가_정확히_설정된다() {
		Member member = Member.create("test@test.com", "encryptedPassword", "테스터");

		assertThat(member.getEmail()).isEqualTo("test@test.com");
		assertThat(member.getPassword()).isEqualTo("encryptedPassword");
		assertThat(member.getName()).isEqualTo("테스터");
	}

	@Test
	void create_id는_null이다() {
		Member member = Member.create("test@test.com", "encryptedPassword", "테스터");

		assertThat(member.getId()).isNull();
	}

	@Test
	void validPassword_비밀번호_일치_정상() {
		Member member = Member.create("test@test.com", "encryptedPassword", "테스터");

		assertThatNoException().isThrownBy(() -> member.validPassword("encryptedPassword"));
	}

	@Test
	void validPassword_비밀번호_불일치_예외발생() {
		Member member = Member.create("test@test.com", "encryptedPassword", "테스터");

		assertThatThrownBy(() -> member.validPassword("wrongPassword"))
				.isInstanceOf(NotValidException.class);
	}
}