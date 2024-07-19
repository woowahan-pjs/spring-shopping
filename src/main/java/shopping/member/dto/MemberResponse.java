package shopping.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import shopping.member.domain.Member;
import shopping.member.domain.Members;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class MemberResponse {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MembersRes {
        private int totalCnt;
        private List<MemberDetail> members;

        public static MembersRes from(Members members) {
            return MembersRes.builder()
                    .totalCnt(members.getTotCnt())
                    .members(addMembers(members.getMembers()))
                    .build();
        }

        private static List<MemberDetail> addMembers(List<Member> members) {
            return members.stream()
                    .map(MemberDetail::from)
                    .toList();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberDetail {

        private Long mbrSn;
        private String email;
        private String name;
        private String delYn;
        private LocalDateTime regDt;

        public static MemberDetail from(Member persistMember) {
            return MemberDetail.builder()
                    .mbrSn(persistMember.getMbrSn())
                    .email(persistMember.getEmail())
                    .name(persistMember.getMbrNm())
                    .delYn(persistMember.getDelYnStr())
                    .regDt(persistMember.getRegDt())
                    .build();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ValidEmail {
        private boolean isValid;
        private String email;

        public static ValidEmail of(boolean isExists, String email) {
            return  ValidEmail.builder()
                    .isValid(!isExists)
                    .email(email)
                    .build();
        }
    }
}
