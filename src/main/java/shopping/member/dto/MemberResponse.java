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
    public static class MemberDetail {

        private Long mbrSn;
        private String email;
        private String name;
        private String delYn;

        public static MemberDetail from(Member persistMember) {
            return MemberDetail.builder()
                    .mbrSn(persistMember.getMbrSn())
                    .email(persistMember.getEmail())
                    .name(persistMember.getMbrNm())
                    .delYn(persistMember.getDelYnStr())
                    .build();
        }
    }
}
