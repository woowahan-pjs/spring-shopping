package shopping.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import shopping.member.domain.Member;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class MemberResponse {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Members {
        private int totalCnt;
        private List<MemberDetail> members;

        public static Members from(List<Member> members) {
            return Members.builder()
                    .totalCnt(members.size())
                    .members(addMembers(members))
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
}
