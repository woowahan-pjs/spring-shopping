package shopping.auth;

public class CustomUserPrincipal {
    private final Long memberId;

    public CustomUserPrincipal(Long memberId) {
        this.memberId = memberId;
    }

    public Long getMemberId() {
        return memberId;
    }
}
