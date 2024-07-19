package shopping.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import shopping.member.domain.Member;

@Data
public class MemberResponse {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegMemberResponse {

        private Long mbrSn;
        private String email;

        public static RegMemberResponse from(Member persistMember) {
            return RegMemberResponse.builder()
                    .mbrSn(persistMember.getMbrSn())
                    .email(persistMember.getEmail())
                    .build();
        }
    }
}
