package shopping.member.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import shopping.common.ApiException;
import shopping.common.ErrorCode;

@DisplayName("[회원] 회원 도메인 단위 테스트")
class MemberTest {
    @Test
    @DisplayName("회원을 만들면 전달한 값으로 초기화한다")
    void createInitializeFields() {
        Member member = MemberFixture.member("user@example.com", "encoded-password", MemberStatus.ACTIVE, MemberRole.USER);

        assertThat(member.getEmail()).isEqualTo("user@example.com");
        assertThat(member.getPassword()).isEqualTo("encoded-password");
        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
        assertThat(member.getRole()).isEqualTo(MemberRole.USER);
        assertThat(member.isActive()).isTrue();
    }

    @Test
    @DisplayName("비밀번호를 바꾸면 새 비밀번호를 저장한다")
    void changePasswordUpdatePassword() {
        Member member = MemberFixture.member("user@example.com", "old-password", MemberStatus.ACTIVE, MemberRole.USER);

        member.changePassword("new-password");

        assertThat(member.getPassword()).isEqualTo("new-password");
    }

    @Test
    @DisplayName("회원을 비활성화하면 상태를 INACTIVE로 바꾼다")
    void deactivateUpdateStatus() {
        Member member = MemberFixture.member("user@example.com", "password", MemberStatus.ACTIVE, MemberRole.USER);

        member.deactivate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.INACTIVE);
        assertThat(member.isActive()).isFalse();
    }

    @ParameterizedTest(name = "[{index}] 역할={0}, 판매자 여부={1}")
    @CsvSource({
            "USER, false",
            "SELLER, true"
    })
    @DisplayName("역할에 따라 판매자 여부를 구분한다")
    void isSellerReturnRoleState(MemberRole role, boolean expected) {
        Member member = MemberFixture.member("user@example.com", "password", MemberStatus.ACTIVE, role);

        boolean result = member.isSeller();

        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("활성 판매자는 판매자 접근 검증을 통과한다")
    void assertActiveSellerPassWhenMemberIsActiveSeller() {
        Member member = MemberFixture.member("seller@example.com", "password", MemberStatus.ACTIVE, MemberRole.SELLER);

        assertThatCode(member::assertActiveSeller).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("비활성 판매자는 판매자 접근 검증을 통과하지 못한다")
    void assertActiveSellerRejectInactiveSeller() {
        Member member = MemberFixture.member("seller@example.com", "password", MemberStatus.INACTIVE, MemberRole.SELLER);

        assertThatThrownBy(member::assertActiveSeller)
                .isInstanceOf(ApiException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.MEMBER_INACTIVE_FORBIDDEN);
    }

    @Test
    @DisplayName("활성 일반 회원은 판매자 접근 검증을 통과하지 못한다")
    void assertActiveSellerRejectUserRole() {
        Member member = MemberFixture.member("user@example.com", "password", MemberStatus.ACTIVE, MemberRole.USER);

        assertThatThrownBy(member::assertActiveSeller)
                .isInstanceOf(ApiException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.MEMBER_SELLER_REQUIRED);
    }

    @Test
    @DisplayName("비밀번호 검증이 성공하면 로그인 검증을 통과한다")
    void assertCanLoginPassWhenPasswordMatches() {
        Member member = MemberFixture.member("user@example.com", "encoded-password", MemberStatus.ACTIVE, MemberRole.USER);

        assertThatCode(() -> member.assertCanLogin(true))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("비밀번호 검증이 실패하면 로그인 검증을 통과하지 못한다")
    void assertCanLoginRejectWhenPasswordDoesNotMatch() {
        Member member = MemberFixture.member("user@example.com", "encoded-password", MemberStatus.ACTIVE, MemberRole.USER);

        assertThatThrownBy(() -> member.assertCanLogin(false))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.MEMBER_CREDENTIALS_INVALID);
    }
}
