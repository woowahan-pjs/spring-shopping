package shopping.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import shopping.exception.BadRequestException;
import shopping.member.domain.Member;

@Data
public class MemberRequest {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegMember {

        @NotBlank
        private String email;

        @NotBlank
        private String password;

        @NotBlank
        private String passwordConfirm;

        private String name;

        private String nickName;

        public static RegMember of(String password, String email, String name, String nickName) {
            return RegMember.builder()
                    .password(password)
                    .passwordConfirm(password)
                    .email(email)
                    .name(name)
                    .nickName(nickName)
                    .build();
        }

        public void validate() {
            if (!password.equals(passwordConfirm)) {
                throw new BadRequestException("비밀번호 확인이 일치하지 않습니다.");
            }
        }

        public Member toMember() {
            return Member.builder()
                    .email(email)
                    .password(password)
                    .mbrNm(name)
                    .nickNm(nickName)
                    .build();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ModMember {

        private String password;

        private String name;

        private String nickName;

        public Member toMember() {
            return Member.builder()
                    .password(password)
                    .mbrNm(name)
                    .nickNm(nickName)
                    .build();
        }
    }
}