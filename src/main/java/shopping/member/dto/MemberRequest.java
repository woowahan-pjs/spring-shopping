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

        public static RegMember of(String password, String email) {
            return RegMember.builder()
                    .password(password)
                    .passwordConfirm(password)
                    .email(email)
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
                    .password(password) //todo 암호화
                    .mbrNm(name)
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

        public Member toMember() {
            return Member.builder()
                    .password(password) //todo 암호화
                    .mbrNm(name)
                    .build();
        }
    }
}
